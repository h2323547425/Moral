import "image-search-google";

function image() {
    const imageSearch = require('image-search-google');

    const client = new imageSearch('CSE ID', 'API KEY');
    const options = {page:1};
    client.search('APJ Abdul kalam', options)
        .then(images => {
            /*
            [{
                'url': item.link,
                'thumbnail':item.image.thumbnailLink,
                'snippet':item.title,
                'context': item.image.contextLink
            }]
             */
        })
        .catch(error => console.log(error));

    return
}

export default image;