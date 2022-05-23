import React, {useState} from 'react';
import axios from 'axios';
import keys from '../../secret/googleApiKey.json';

function Logo(props: { company: string }) {

    const [image, setImage] = useState("");

    const getImage = (name: string) => {
        name = name.split(" ").join("%20");
        const url = `https://www.googleapis.com/customsearch/v1?key=${keys["api_key"]}`
            + `&cx=${keys["engine_id"]}&q=${name}%20logo&searchType=image&alt=json`;
        console.log(keys);
        axios.get(url).then((res) => {
            console.log("Google Search API succeeded from: " + url);
            const img = res.data.items[0].link;
            setImage(img);
        }).catch((err) => {
            console.log("Google Search API failed with error: ", err);
        });
        return (<a href={`https://www.google.com/search?q=${name}%20stock`} target="_blank">
                <img src={image} alt={"image for company"} width="400"/>
            </a>
        )
    };

    return (
        <div>
            {getImage(props.company)}
        </div>
    )
}

export default Logo;