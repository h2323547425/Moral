import React from "react";
import "./login.css";
import {GoogleLogout} from "react-google-login";
import {useNavigate} from "react-router-dom";

/**
 * Component that represents the Google logout button
 * @return the Google logout button
 */
function GoogleLogoutButton() {

    const clientID: string = "634750823038-vvs000oi60ovd1r51nnso7u448j3t9ho.apps.googleusercontent.com"
    let navigate = useNavigate();

    return (
            <GoogleLogout
                clientId={clientID}
                buttonText="Logout"
                onLogoutSuccess={() => navigate("/")}
            />
    );

}

export default GoogleLogoutButton