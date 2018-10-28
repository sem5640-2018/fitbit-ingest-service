import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "token_map")
public class TokenMap implements Serializable {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private String userID;

    @Column(name= "access_token")
    private String accessToken;

    @Column(name= "refresh_token")
    private String refreshToken;

    public TokenMap() {
        //
    }

    public TokenMap(String userID, String accessToken) {
        this.userID = userID;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
