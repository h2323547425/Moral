import React from 'react';
import LikeButton from "../favorites/LikeButton";

/**
 * This component is to generate one specific stock shown on the suggested stocks panel
 * @param props
 * email: the email address of the user
 * stockName: the name of the stock company
 * abbrev the abbrev of the stocks
 * score the score of the stocks genenerated by the value
 * yields: string, the yields for this specific stocks
 * marketCap: string, the marketCap for this specific stock
 * peratio: string the peratio for this specific stock
 * doWellIn: the fields the stock does well in
 * setStockName: (value: string) => void,
 * setDisplayScore: (value: string) => void,
 * setDoWellIn: (value: string) => void,
 * setDisplayPrice: (value: string) =>void,
 * setPeratio: (value: string) => void,
 * setMarketCap: (value: string) => void,
 * setYeilds: (value: string) => void,
 * @constructor
 */
function Box(props: {
    email: string
    stockName: string,
    abbrev: string,
    price: string,
    score: string,
    doWellIn: string,
    yields: string,
    marketCap: string,
    peratio: string
    setStockName: (value: string) => void,
    setDisplayScore: (value: string) => void,
    setDoWellIn: (value: string) => void,
    setDisplayPrice: (value: string) =>void,
    setPeratio: (value: string) => void,
    setMarketCap: (value: string) => void,
    setYeilds: (value: string) => void,
    faveList: string[],
    getFaveList: (value: string) => void
}) {
    return (
        // This part of the code is to generate the view of the score, abbrev and also the price
        <div>
            <div id={props.stockName} className="stock"  onClick={() => {
                props.setStockName(props.stockName);
                props.setDisplayScore(props.score);
                props.setDisplayPrice(props.price);
                props.setDoWellIn(props.doWellIn);
                props.setMarketCap(props.marketCap);
                props.setPeratio(props.peratio);
                props.setYeilds(props.yields)
            }}>
                <div>
            <div className={"container-small"}>
                <div className={"stockname"}>
                    <h1 className={"font"} onClick={() => {
                        props.setStockName(props.stockName);
                        props.setDisplayScore(props.score);
                        props.setDisplayPrice(props.price);
                        props.setDoWellIn(props.doWellIn);
                        props.setMarketCap(props.marketCap);
                        props.setPeratio(props.peratio);
                        props.setYeilds(props.yields)
                    }}> {props.abbrev} </h1>
                    {/*<h2 className={"font-little"} onClick={() => {*/}
                    {/*    props.setStockName(props.stockName);*/}
                    {/*    props.setDisplayScore(props.score);*/}
                    {/*    props.setDisplayPrice(props.price);*/}
                    {/*    props.setDoWellIn(props.doWellIn);*/}
                    {/*}}> {props.stockName}</h2>*/}
                </div>
                <div id="price" className={"price"}>
                    <h1> ${props.price}</h1>
                </div>
                <div id="price" className={"score"}>
                    <h1> Score: {props.score}</h1>
                </div>
            </div>
                <div>
                <h2 className={"font-little"} onClick={() => {
                    props.setStockName(props.stockName);
                    props.setDisplayScore(props.score);
                    props.setDisplayPrice(props.price);
                    props.setDoWellIn(props.doWellIn);
                    props.setMarketCap(props.marketCap);
                    props.setPeratio(props.peratio);
                    props.setYeilds(props.yields)
                }}> {props.stockName}</h2>
                </div>
            </div>
                <LikeButton email={props.email} stock={props.abbrev} getFaveList={props.getFaveList} favoriteList={props.faveList}/>
            </div>
        </div>
    )
}

export default Box