import React from 'react';
import './user.css';
import Logo from './Logo';

/**
 * This is a component that can be seen at the right side
 * @param props
 stockname: the name of the stock that is to be shown
 doWellIn: the fields that this stock do well in
 dprice: the price of this stocks calculated based ont the filter values
 stockDescription: the description
 pictureUrl: string,
 score: the score of stockname calculated based on the filter values
 * @constructor
 */
function Company(props: {
    stockname: string,
    doWellIn: string,
    dprice: string
    stockDiscription: string[],
    pictureUrl: string,
    score: string,
    peratio: string,
    yields: string,
    marketCap: string
}) {
    const fields = props.doWellIn.split(",").slice(0, -1)
    return (
        <div className={"company-block"}>
            <h1 className={"font-title"}> Company View </h1>
            <div className={"one-stock-view"}>
                <div className={"logo-company"}>
                    <Logo company={props.stockname}/>
                </div>
                <div className={"description-display"}>
                    <h1 className={"font"}>{props.stockname}</h1>
                </div>
                <div className={"parent3"}>
                    <div className={"field_do_well"}>
                        {fields.map((cell) => <p> ✅ {cell}</p>)}
                    </div>
                    <div className={"company-score"}>
                        <h3> 🌱 Score: {props.score}</h3>
                        <h3> 💵 Price: {props.dprice}</h3>
                        <h3> 📈 Market Cap: {props.marketCap}</h3>
                        <h3> 🧮 P/E Ratio: {props.peratio}</h3>
                        <h3> 📜 Dividend Yield: {props.yields}</h3>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Company