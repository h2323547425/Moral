import React from 'react';

import {useNavigate} from "react-router-dom";
import GoogleLogoutButton from "../login/GoogleLogoutButton";

/**
 * Component for the navigation bar
 * @return the top bar for the webpage
 */
function Topbar() {

    let navigate = useNavigate();

    return (
        <header>
            <div className="nav-bar">
                <div className="account">
                    <button className="user-button"/>
                    <div className="googleLogoutButton">
                        <GoogleLogoutButton />
                    </div>
                </div>
                <div className="logo">
                    <img src={require('../../images/moral_logo.png')} className="logo-image" onClick={() => {
                        navigate("/home");
                    }}/>
                </div>
                <div className="heart">
                    <img src={require('../../images/favorites-heart.png')} className="heart-image" onClick={() => {
                        navigate("/favorites");
                    }}/>
                </div>
                <nav className={"nav-about"}>
                    <ul>
                        <li><a href="#" onClick={() => {
                            navigate("/about");
                        }}>About</a></li>
                    </ul>
                </nav>
            </div>
        </header>
    )

}

export default Topbar