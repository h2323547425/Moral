import React from 'react';
import FavoritesBox from './FavoritesBox'
import sortByDropdown from "./SortByDropdown";

/**
 * Component that represents the favorite stocks on the favorites page
 * @param props - email, faveList, getFaveList, favoriteList, stockDetails
 * @return the box containing all of a user's favorite stocks
 */
function FavoritesBoxes(props: {
    email: string,
    faveList: string[],
    getFaveList: (value: string) => void,
    favoriteList: string[],
    stockDetails: any,
    sortedList: string[],
    setSortedList: (value: string[]) => void
}) {

    if (props.sortedList.length == 0) {
        props.setSortedList(props.faveList)
    }
    const newFaveList = props.sortedList.filter(stock => props.faveList.some(other => other === stock))

    return (
        <div className="fave-stock-list-block">
            {newFaveList.map(stock => <FavoritesBox email={props.email} getFaveList={props.getFaveList}
                                                       stockName={stock}
                                                       favoriteList={props.favoriteList}
                                                       oneStockDetails={props.stockDetails[stock]}/>)}
        </div>
    )

}

export default FavoritesBoxes