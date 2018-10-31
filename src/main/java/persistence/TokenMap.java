package persistence;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Client Credential record in the fitbit ingest database.
 * @author James Britton
 * @author jhb15@aber.ac.uk
 * @version 0.1
 */
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

    /**
     * Empty Constructor
     */
    public TokenMap() {
        //
    }

    /**
     * Constructor that takes parameters.
     * @param userID user id of current user
     * @param accessToken access token obtained from fitbit
     * @param refreshToken refresh token obtained from fitbit
     */
    public TokenMap(String userID, String accessToken, String refreshToken) {
        this.userID = userID;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /**
     * getter fot the record id.
     * @return record id
     */
    public Long getId() {
        return id;
    }

    /**
     * setter for the record id.
     * @param id desired id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter for the user id in Token Map
     * @return user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * setter for the user id in Token Map
     * @param userID desired user id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * getter for the access token in the Token Map
     * @return access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * setter for access token in the Token Map
     * @param accessToken desired access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * getter for the refresh token in the Token Map
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * setter for the refresh token in the Token Map
     * @param refreshToken desired refresh token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
