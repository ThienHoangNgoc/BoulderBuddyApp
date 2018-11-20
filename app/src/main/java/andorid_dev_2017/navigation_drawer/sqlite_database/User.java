package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.graphics.Bitmap;

public class User {


    String id;
    String username;
    String password;
    String lastLogin;
    String exp;
    String rank;
    String rankPoints;
    String imagePath;
    String loginStatus;


    public User(String id, String username, String password,
                String lastLogin, String exp, String rank,
                String rankPoints, String profileImage, String loginStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLogin = lastLogin;
        this.exp = exp;
        this.rank = rank;
        this.rankPoints = rankPoints;
        this.imagePath = profileImage;

        this.loginStatus = loginStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(String rankPoints) {
        this.rankPoints = rankPoints;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }


}
