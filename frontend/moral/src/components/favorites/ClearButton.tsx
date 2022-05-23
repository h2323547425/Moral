import React from 'react';

/**
 * Component that represents the clear favorites button
 * @param props - email, faveList, setFaveList, getFaveList, refreshFaves, setRefreshFaves
 * @return the clear favorites button
 */
function ClearButton(props: {
    email: string,
    faveList: string[],
    getFaveList: (value: string) => void,
    refreshFaves: boolean,
    setRefreshFaves: (value: boolean) => void
}) {

    /**
     * Async function that clears the favorites list for a user
     */
    const handleClear = async () => {
        let URL = "http://localhost:4567/favorites/delete";
        const body = {
            username: props.email,
            stock: "Clear all"
        }
        const requestOptions = {
            method: 'POST',
            headers: {"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
            body: JSON.stringify(body)
        };
        await fetch(URL, requestOptions)
            .then((response) => {
                props.getFaveList(props.email)
            })
            .catch(error => {
                console.log(error);
            })
        props.setRefreshFaves(!props.refreshFaves)
    };

    return (
        <a href="#" className={"clear"} onClick={handleClear}>Clear All</a>
    )

}

export default ClearButton;