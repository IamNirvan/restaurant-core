package com.iamnirvan.restaurant.core.models.requests.customer;

import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
/*
    private String username;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "password must contain at least one lowercase letter, one uppercase letter, " +
                    "one digit, one special character, and must be at least 8 characters long"
    )
*/
//    private String password;
    private AccountUpdateRequest accountInfo;
    private String firstName;
    private String lastName;
}
