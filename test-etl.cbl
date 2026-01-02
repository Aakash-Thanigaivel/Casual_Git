      * ETL Customer Data Processing System
       IDENTIFICATION DIVISION.
       PROGRAM-ID. ETL-CUSTOMER-PROCESS.
       AUTHOR. TEST-MIGRATION.
       DATE-WRITTEN. 2024-01-02.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT CUSTOMER-INPUT ASSIGN TO 'CUSTOMER.DAT'
               ORGANIZATION IS LINE SEQUENTIAL.
           SELECT PROCESSED-OUTPUT ASSIGN TO 'PROCESSED.DAT'
               ORGANIZATION IS LINE SEQUENTIAL.
           SELECT ERROR-OUTPUT ASSIGN TO 'ERRORS.DAT'
               ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD  CUSTOMER-INPUT.
       01  CUSTOMER-RECORD.
           05  CUST-ID                PIC 9(8).
           05  CUST-NAME              PIC X(30).
           05  CUST-ACCOUNT-TYPE      PIC X(1).
               88  CHECKING-ACCOUNT   VALUE 'C'.
               88  SAVINGS-ACCOUNT    VALUE 'S'.
               88  BUSINESS-ACCOUNT   VALUE 'B'.
           05  CUST-BALANCE           PIC 9(7)V99.
           05  CUST-CREDIT-LIMIT      PIC 9(7)V99.
           05  CUST-STATUS            PIC X(1).
               88  ACTIVE-CUSTOMER    VALUE 'A'.
               88  INACTIVE-CUSTOMER  VALUE 'I'.
               88  SUSPENDED-CUSTOMER VALUE 'S'.

       FD  PROCESSED-OUTPUT.
       01  PROCESSED-RECORD.
           05  PROCESSED-ID           PIC 9(8).
           05  PROCESSED-NAME         PIC X(30).
           05  PROCESSED-ACCOUNT-TYPE PIC X(15).
           05  PROCESSED-BALANCE      PIC 9(9)V99.
           05  PROCESSED-AVAILABLE-CREDIT PIC 9(9)V99.
           05  PROCESSED-RISK-LEVEL   PIC X(10).
           05  PROCESSED-PROCESS-DATE PIC X(10).

       FD  ERROR-OUTPUT.
       01  ERROR-RECORD.
           05  ERROR-ID               PIC 9(8).
           05  ERROR-NAME             PIC X(30).
           05  ERROR-TYPE             PIC X(20).
           05  ERROR-DESCRIPTION      PIC X(50).
           05  ERROR-PROCESS-DATE     PIC X(10).

       WORKING-STORAGE SECTION.
       01  WS-COUNTERS.
           05  WS-RECORDS-READ        PIC 9(6) VALUE ZERO.
           05  WS-RECORDS-PROCESSED   PIC 9(6) VALUE ZERO.
           05  WS-RECORDS-ERROR       PIC 9(6) VALUE ZERO.
           05  WS-TOTAL-BALANCE       PIC 9(10)V99 VALUE ZERO.
           05  WS-AVERAGE-BALANCE     PIC 9(8)V99 VALUE ZERO.

       01  WS-WORK-AREAS.
           05  WS-AVAILABLE-CREDIT    PIC 9(7)V99.
           05  WS-RISK-LEVEL          PIC X(10).
           05  WS-CURRENT-DATE.
               10  WS-YEAR            PIC 9(4).
               10  WS-MONTH           PIC 9(2).
               10  WS-DAY             PIC 9(2).
           05  WS-FORMATTED-DATE      PIC X(10).

       01  WS-CONSTANTS.
           05  HIGH-RISK-THRESHOLD    PIC 9(7)V99 VALUE 50000.00.
           05  MEDIUM-RISK-THRESHOLD  PIC 9(7)V99 VALUE 25000.00.
           05  MINIMUM-BALANCE        PIC 9(5)V99 VALUE 100.00.

       PROCEDURE DIVISION.
       MAIN-PROCESS.
           PERFORM INITIALIZE-PROCESS
           PERFORM PROCESS-CUSTOMER-FILE
           PERFORM GENERATE-REPORT
           PERFORM CLEANUP-PROCESS
           STOP RUN.

       INITIALIZE-PROCESS.
           OPEN INPUT CUSTOMER-INPUT
           OPEN OUTPUT PROCESSED-OUTPUT
           OPEN OUTPUT ERROR-OUTPUT

           MOVE FUNCTION CURRENT-DATE(1:8) TO WS-CURRENT-DATE
           STRING WS-YEAR '-' WS-MONTH '-' WS-DAY
               DELIMITED BY SIZE INTO WS-FORMATTED-DATE

           DISPLAY 'ETL Process Started on ' WS-FORMATTED-DATE.

       PROCESS-CUSTOMER-FILE.
           READ CUSTOMER-INPUT
               AT END MOVE 'Y' TO END-OF-FILE
           END-READ

           PERFORM UNTIL END-OF-FILE
               ADD 1 TO WS-RECORDS-READ

               PERFORM VALIDATE-CUSTOMER-DATA

               IF VALID-CUSTOMER
                   PERFORM TRANSFORM-CUSTOMER-DATA
                   PERFORM LOAD-PROCESSED-DATA
                   ADD 1 TO WS-RECORDS-PROCESSED
                   ADD CUST-BALANCE TO WS-TOTAL-BALANCE
               ELSE
                   PERFORM LOAD-ERROR-DATA
                   ADD 1 TO WS-RECORDS-ERROR
               END-IF

               READ CUSTOMER-INPUT
                   AT END MOVE 'Y' TO END-OF-FILE
               END-READ
           END-PERFORM.

       VALIDATE-CUSTOMER-DATA.
           MOVE 'Y' TO VALID-CUSTOMER-FLAG

           IF CUST-ID = ZERO
               MOVE 'N' TO VALID-CUSTOMER-FLAG
               MOVE 'INVALID CUSTOMER ID' TO WS-ERROR-TYPE
           END-IF

           IF CUST-NAME = SPACES
               MOVE 'N' TO VALID-CUSTOMER-FLAG
               MOVE 'MISSING CUSTOMER NAME' TO WS-ERROR-TYPE
           END-IF

           IF NOT (CHECKING-ACCOUNT OR SAVINGS-ACCOUNT OR BUSINESS-ACCOUNT)
               MOVE 'N' TO VALID-CUSTOMER-FLAG
               MOVE 'INVALID ACCOUNT TYPE' TO WS-ERROR-TYPE
           END-IF

           IF CUST-BALANCE < MINIMUM-BALANCE AND ACTIVE-CUSTOMER
               MOVE 'N' TO VALID-CUSTOMER-FLAG
               MOVE 'BALANCE BELOW MINIMUM' TO WS-ERROR-TYPE
           END-IF.

       TRANSFORM-CUSTOMER-DATA.
           * Calculate available credit
           COMPUTE WS-AVAILABLE-CREDIT = CUST-CREDIT-LIMIT - CUST-BALANCE

           * Determine risk level based on balance and credit utilization
           EVALUATE TRUE
               WHEN CUST-BALANCE > HIGH-RISK-THRESHOLD
                   MOVE 'HIGH RISK' TO WS-RISK-LEVEL
               WHEN CUST-BALANCE > MEDIUM-RISK-THRESHOLD
                   MOVE 'MEDIUM RISK' TO WS-RISK-LEVEL
               WHEN OTHER
                   MOVE 'LOW RISK' TO WS-RISK-LEVEL
           END-EVALUATE

           * Format account type for output
           EVALUATE TRUE
               WHEN CHECKING-ACCOUNT
                   MOVE 'CHECKING' TO PROCESSED-ACCOUNT-TYPE
               WHEN SAVINGS-ACCOUNT
                   MOVE 'SAVINGS' TO PROCESSED-ACCOUNT-TYPE
               WHEN BUSINESS-ACCOUNT
                   MOVE 'BUSINESS' TO PROCESSED-ACCOUNT-TYPE
           END-EVALUATE.

       LOAD-PROCESSED-DATA.
           MOVE CUST-ID TO PROCESSED-ID
           MOVE CUST-NAME TO PROCESSED-NAME
           MOVE CUST-BALANCE TO PROCESSED-BALANCE
           MOVE WS-AVAILABLE-CREDIT TO PROCESSED-AVAILABLE-CREDIT
           MOVE WS-RISK-LEVEL TO PROCESSED-RISK-LEVEL
           MOVE WS-FORMATTED-DATE TO PROCESSED-PROCESS-DATE

           WRITE PROCESSED-RECORD.

       LOAD-ERROR-DATA.
           MOVE CUST-ID TO ERROR-ID
           MOVE CUST-NAME TO ERROR-NAME
           MOVE WS-ERROR-TYPE TO ERROR-TYPE
           MOVE 'CUSTOMER RECORD REJECTED' TO ERROR-DESCRIPTION
           MOVE WS-FORMATTED-DATE TO ERROR-PROCESS-DATE

           WRITE ERROR-RECORD.

       GENERATE-REPORT.
           IF WS-RECORDS-READ > ZERO
               COMPUTE WS-AVERAGE-BALANCE = WS-TOTAL-BALANCE / WS-RECORDS-PROCESSED
           END-IF

           DISPLAY 'ETL PROCESS SUMMARY REPORT'
           DISPLAY '=========================='
           DISPLAY 'Records Read: ' WS-RECORDS-READ
           DISPLAY 'Records Processed: ' WS-RECORDS-PROCESSED
           DISPLAY 'Records Rejected: ' WS-RECORDS-ERROR
           DISPLAY 'Total Balance: $' WS-TOTAL-BALANCE
           DISPLAY 'Average Balance: $' WS-AVERAGE-BALANCE
           DISPLAY 'Process Date: ' WS-FORMATTED-DATE.

       CLEANUP-PROCESS.
           CLOSE CUSTOMER-INPUT
           CLOSE PROCESSED-OUTPUT
           CLOSE ERROR-OUTPUT

           DISPLAY 'ETL Process Completed Successfully'.

       END PROGRAM ETL-CUSTOMER-PROCESS.
