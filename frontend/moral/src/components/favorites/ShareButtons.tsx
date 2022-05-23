import React from 'react';

import {
    EmailShareButton,
    EmailIcon,
    FacebookShareButton,
    FacebookIcon,
    TwitterShareButton,
    TwitterIcon
} from "react-share";

/**
 * Component that represents the share buttons on the favorites page
 * @return the share buttons on the favorites page
 */
function ShareButtons() {

    return (
        <div className="share-buttons">
            <EmailShareButton
                url={"http://127.0.0.1/"}
                subject={"My Moral Favorites"}
            >
                <EmailIcon size={35} round/>
            </EmailShareButton>
            &nbsp;
            <TwitterShareButton
                title={"My Moral Favorites"}
                url={"http://127.0.0.1/"}
                hashtags={["Moral"]}
            >
                <TwitterIcon size={35} round/>
            </TwitterShareButton>
            &nbsp;
            <FacebookShareButton
                url={"http://127.0.0.1/"}
                quote={"Moral - Invest ethically. Invest sustainably."} // ???
                hashtag={"#Moral"}
            >
                <FacebookIcon size={35} round/>
            </FacebookShareButton>
        </div>
    )

}

export default ShareButtons