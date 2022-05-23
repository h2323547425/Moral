import React, {useState} from 'react';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Favorite from '@material-ui/icons/Favorite';
import FavoriteBorder from '@material-ui/icons/FavoriteBorder';

/**
 * Component that represents the like button
 * @param props - email, stock, favoriteList, getFaveList
 * @return the like button
 */
function LikeButton(props: {
    email: string,
    stock: string,
    favoriteList: string[],
    getFaveList: (value: string) => void
}) {
    // hook that keeps track of whether the like button is pressed
    const [isLiked, setLike] = useState(props.favoriteList.includes(props.stock));
    
    const handleLike = async () => {
        setLike(!isLiked)
        let URL = "http://localhost:4567/favorites/";
        if (!isLiked) {
            URL += "add"
        } else {
            URL += "delete"
        }
        const body = {
            username: props.email,
            stock: props.stock
        }
        const requestOptions = {
            method: 'POST',
            headers: {"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
            body: JSON.stringify(body)
        };
        await fetch(URL, requestOptions)
            .then(() => {
                props.getFaveList(props.email)
            })
            .catch(error => {
                console.log(error);
            })
    };

    return (
        <div className="LikeButton">
            <FormControlLabel
                control={<Checkbox icon={<FavoriteBorder/>}
                                   checked = {isLiked}
                                   checkedIcon={<Favorite/>}
                                   name="Liked"/>}
                label=""
                onClick={handleLike}
            />
        </div>
    );

}

export default LikeButton;