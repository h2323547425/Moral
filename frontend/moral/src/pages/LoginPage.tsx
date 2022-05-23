import React from 'react';
import "../components/login/login.css";
import GoogleLoginButton from "../components/login/GoogleLoginButton";

/**
 * Component that represents the login page
 * @param props - setEmail, getFaveList, getStockDetails, getSortedList
 * @return the login page
 */
function LoginPage(props: {
    setEmail: (value: string) => void,
    getFaveList: (value: string) => void,
    getStockDetails: (value: void) => void,
    getSortedList: (value: string) => void
}) {

    document.body.style.backgroundImage = "url(http://localhost:3000/static/media/login_page.2524abae2bf50f39b37e.jpg)";
    document.body.style.backgroundSize = "cover";

    return (
        <div className="LoginPage">
            <GoogleLoginButton setEmail={props.setEmail} getFaveList={props.getFaveList}
                               getStockDetails={props.getStockDetails}
                               getSortedList={props.getSortedList}/>
        </div>
    );

}

export default LoginPage
