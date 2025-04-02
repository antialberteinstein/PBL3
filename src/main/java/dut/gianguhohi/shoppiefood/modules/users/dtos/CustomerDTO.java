package dut.gianguhohi.shoppiefood.modules.users.dtos;

public class CustomerDTO {  

    private final int id;
    private final String name;
    private final String phoneNumber;
    private final String dob;
    private final int gender;
    // 0 for female.
    // 1 for male.
    // 2 for other.

    public CustomerDTO(int id, String name, String phoneNumber, String dob, int gender) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public int getGender() {
        return gender;
    }

}
