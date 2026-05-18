package Mangement.StudentManagement.Entity;
import jakarta.persistence.*;
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;
    private String city;
    private String state;
    private String pincode;
    public void setAddressId(int addressId){
        this.addressId=addressId;
    }
    public int getAddressId(){
        return addressId;
    }
    public void setCity(String city){
        this.city=city;
    }
    public String getCity(){
        return city;
    }
    public void setState(String state){
        this.state=state;
    }
    public String getState(){
        return state;
    }
    public void setPincode(String pincode){
        this.pincode=pincode;
    }
    public String getPincode(){
        return pincode;
    }
}
