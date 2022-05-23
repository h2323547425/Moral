import React from 'react';
import LikeButton from "./LikeButton";
import Characteristic from "./Characteristic";

/**
 * Component that represents one box of stock info on the favorites page
 * @param props - email, getFaveList, stockName, oneStockDetails, favoriteList
 * @return a box of stock info
 */
function FavoritesBox(props: {
    email: string,
    getFaveList: (value: string) => void,
    stockName: string,
    oneStockDetails: any
    favoriteList: string[]
}) {

    return (
        <div>
            <div id={props.stockName} className="fave-stock">
                <div className="stockname">
                    <h1 className={"font"}> {props.stockName} </h1>
                    <h2 className={"full-company-name"}> {props.oneStockDetails["name"]}</h2>
                </div>
                <Characteristic feature={"score"} value={props.oneStockDetails["Score"]}/>
                <Characteristic feature={"price"} value={"$" + props.oneStockDetails["Price"]}/>
                <Characteristic feature={"P/E ratio"} value={props.oneStockDetails["PE_Ratio"]}/>
                <Characteristic feature={"market cap"} value={props.oneStockDetails["Market_Cap"]}/>
                <Characteristic feature={"dividend yields"} value={props.oneStockDetails["dividend yield"]}/>
                <LikeButton email={props.email} stock={props.stockName} getFaveList={props.getFaveList}
                            favoriteList={props.favoriteList}/>
            </div>
        </div>
    )

}

export default FavoritesBox