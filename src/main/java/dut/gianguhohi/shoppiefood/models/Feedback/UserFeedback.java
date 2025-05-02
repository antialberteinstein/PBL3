package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Users.Users;

@Entity
@DiscriminatorValue("USER")
public class UserFeedback extends Feedback {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public UserFeedback() {
        super();
    }

    public UserFeedback(int type, String content, Users user) {
        super(type, content);
        this.user = user;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
