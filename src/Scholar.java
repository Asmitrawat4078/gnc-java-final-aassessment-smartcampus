import java.io.Serializable;

public class Scholar implements Serializable {
    
    private int scholarId;
    private String fullName;
    private String contactEmail;

    public Scholar(int scholarId, String fullName, String contactEmail) {
        this.scholarId = scholarId;
        this.fullName = fullName;
        this.contactEmail = contactEmail;
    }

    public int getScholarId() { return scholarId; }
    public void setScholarId(int scholarId) { this.scholarId = scholarId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    @Override
    public String toString() {
        return "Scholar ID: " + scholarId + " | Name: " + fullName + " | Email: " + contactEmail;
    }
}
/*
Ye class system me student ki details (entity) ko represent karti hai. Ye Serializable implement karti hai 
taaki student ka data direct file me save aur load ho sake. Data ko safe rakhne ke liye isme encapsulation ka use hua hai.
*/
