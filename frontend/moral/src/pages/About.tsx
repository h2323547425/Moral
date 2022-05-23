import React from 'react';

/**
 * Component that contains the necessary info on the about page
 * @return the information on the about page
 */
function About() {

    return (
        <div className={"about"}>
            <img src={require('../images/plant.png')}/>
            <div className={"about-text"}>
                <h1> We make ethical <br/> investment simple. </h1>
                <p> Deciding between wanting environmental, social, and governance (ESG) factors <br/>
                    and financial data on stocks can be difficult, so why not have them both? <br/>
                    Rather than going through multiple sources to obtain this information, Moral <br/>
                    allows users to accomplish all of this in one place, through the use of filters <br/>
                    to specify the weight placed on certain ethical characteristics. <br/> </p>
                <p> Simply use the sliding bars to indicate the importance of the ethical categories, <br/>
                    and a list of stocks will be generated in accordance with the indicated <br/>
                    preferences. Moral gives users the ability to save stocks to a designated <br/>
                    favorites page, making accessing the desired stocks easier than ever.<br/> </p>
            </div>
        </div>
    )

}

export default About;