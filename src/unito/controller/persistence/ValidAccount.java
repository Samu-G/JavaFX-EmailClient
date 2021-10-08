package unito.controller.persistence;

import java.io.Serializable;

public class ValidAccount implements Serializable {

    private String address;
    private String password;

    public ValidAccount(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            System.out.println("oggetto null");
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            System.out.println("classe non corretta");
            return false;
        }
        else {
            System.out.println("confronto " + this.getAddress() + " con " + ((ValidAccount) obj).getAddress());
            System.out.println("confronto " + this.getPassword() + " con " + ((ValidAccount) obj).getPassword());
            if (this.address == ((ValidAccount) obj).getAddress()
                    && this.password == ((ValidAccount) obj).getPassword()) {
                return true;
            } else {
                return false;
            }
        }
    }

}
