import React from 'react';

import { useNavigate } from "react-router-dom";

/**
 * Component for returning to the home page
 * @return a button for the user to return to the home page
 */
function Return(){

    let navigate = useNavigate();
    return(
            <a href="#" className={"return-home"} onClick={() => {navigate("/home");}} > &#60; Return Home</a>
    )

}
export default Return;