import React from 'react';
import Topbar from "../components/userview/Topbar";
import About from "./About";
import Return from "../components/favorites/Return";

/**
 * Component that represents the about page
 * @return the about page
 */
function AboutPage(){
    document.body.style.background = "#EFEDE9";
    return(
        <div className={"about-page"}>
            <Topbar/>
            <Return/>
            <About/>
        </div>
    )
}
export default AboutPage;