package app.entities;

public class User
{
    private int id;
    private String firstName;
    private String lastName;
    private int zipCode;
    private String streetName;
    private Integer houseNumber;
    private String floor;
    private String email;
    private String password;
    private float wallet;

    public User(int id, String firstName, String lastName, int zipCode, String streetName,
                Integer houseNumber, String floor, String email, String password, float wallet)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.floor = floor;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
    }

    public User(int id, String email, String password)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public Integer getHouseNumber()
    {
        return houseNumber;
    }

    public String getFloor()
    {
        return floor;
    }

    public String getEmail()
    {
        return email;
    }
    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public String getPassword()
    {
        return password;
    }

    public float getWallet()
    {
        return wallet;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", zipCode=" + zipCode +
                ", streetName='" + streetName + '\'' +
                ", houseNumber=" + houseNumber +
                ", floor='" + floor + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}

