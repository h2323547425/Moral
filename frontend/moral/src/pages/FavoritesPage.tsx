import React, {useState} from 'react';
import Topbar from "../components/userview/Topbar";
import FavoritesBoxes from "../components/favorites/FavoritesBoxes";
import "../components/favorites/favoritespage.css";
import SortByDropdown from "../components/favorites/SortByDropdown";
import ShareButtons from "../components/favorites/ShareButtons";
import Return from "../components/favorites/Return";
import ClearButton from "../components/favorites/ClearButton";

/**
 * Component that represents the favorites page
 * @param props - email, faveList, getFaveList, setFaveList, stockDetails
 * @return the favorites page
 */
function FavoritesPage(props: {
    email: string,
    faveList: string[],
    getFaveList: (value: string) => void,
    setFaveList: (value: string[]) => void,
    stockDetails: any
}) {

    document.body.style.background = "#EFEDE9";
    const [refreshFaves, setRefreshFaves] = useState(false)
    const [sortedList, setSortedList] = useState([] as string[])

    return (
        <div>
            <Topbar/>
            <Return/>
            <div className="parent">
                <header className="favorites-header">
                    <h1>Favorites</h1>
                </header>
                <div className="share-label">
                    <label>Share: </label>
                </div>
                <ShareButtons/>
                <div className="dropdown-label">
                    <label>Sort by: </label>
                </div>
                <SortByDropdown faveList={props.faveList} setFaveList={props.setFaveList}
                                stockDetails={props.stockDetails} refreshFaves={refreshFaves}
                                setRefreshFaves={setRefreshFaves} setSortedList={setSortedList} sortedList={sortedList}/>
                <ClearButton email={props.email} faveList={props.faveList}
                             getFaveList={props.getFaveList} refreshFaves={refreshFaves}
                             setRefreshFaves={setRefreshFaves}/>
                <FavoritesBoxes email={props.email} faveList={props.faveList} getFaveList={props.getFaveList}
                                stockDetails={props.stockDetails} favoriteList={props.faveList}
                                sortedList={sortedList} setSortedList={setSortedList}/>
            </div>
        </div>
    )
    
}

export default FavoritesPage