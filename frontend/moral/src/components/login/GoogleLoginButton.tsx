import React from "react";
import "./login.css";
import {GoogleLogin} from "react-google-login";
import {useNavigate} from "react-router-dom";

/**
 * Component that represents the Google login button
 * @param props - setEmail, getFaveList, getStockDetails, getSortedList
 * @return the Google login button
 */
function GoogleLoginButton(props: {
    setEmail: (value: string) => void,
    getFaveList: (value: string) => void,
    getStockDetails: (value: void) => void,
    getSortedList: (value: string) => void,
}) {

    const clientID: string = "634750823038-vvs000oi60ovd1r51nnso7u448j3t9ho.apps.googleusercontent.com"
    let navigate = useNavigate();

    /**
     * Function that sets user info and loads user favorites and stock details once
     * they log in
     * @param response - the Google login response
     */
    const login = (response) => {
        props.setEmail(response.profileObj.email);
        props.getFaveList(response.profileObj.email);
        props.getStockDetails();
        props.getSortedList("0.5,0.5,0.5,0.5,0.5")
        navigate("/home")
    };

    return (
        <div className="googleLoginButton">
            <GoogleLogin
                clientId={clientID}
                buttonText="Login with Google"
                onSuccess={login}
                onFailure={login}
                cookiePolicy={"single_host_origin"}
            />
        </div>
    );

}

export default GoogleLoginButton