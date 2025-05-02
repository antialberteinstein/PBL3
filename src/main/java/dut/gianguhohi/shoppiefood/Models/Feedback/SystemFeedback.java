package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SYSTEM")
public class SystemFeedback extends Feedback {
    public SystemFeedback() {
        super();
    }

    public SystemFeedback(int type, String content) {
        super(type, content);
    }
}
