import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring Boot 6.1 migration example for Bank of America code converter project.
 * Converted from Spring 5 to Spring Boot 6.1 following Google Java Style Guidelines.
 */
@RestController
@RequestMapping("/api")
public class UserController {
    
    /**
     * Retrieves all users using modern Spring Boot 6.1 annotations.
     * Migrated from @RequestMapping to @GetMapping for better REST semantics.
     * 
     * @return List of all users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}