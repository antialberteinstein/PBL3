package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Users.User;
@Entity
@DiscriminatorValue("SYSTEM")
public class SystemFeedback extends Feedback {
    public SystemFeedback() {
        super();
    }

    public SystemFeedback(String content, User whoFeedback) {
        super(content, whoFeedback);
    }
}
