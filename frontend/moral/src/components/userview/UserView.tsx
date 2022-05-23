import React, {useState} from 'react';
import Topbar from "./Topbar";
import Filters from "./Filters";
import Boxes from "./Boxes";
import Company from "./Company";
import "./user.css";

/**
 * This is a class that generate all the whole userview panel after the user login
 * @param props
 email: the email of this specfic user
 faveList: a list that stock all the abrev that this user like
 sortedScore: a map that sort the score of the stocks in descending order
 getFaveList: this is used to update the favorite list of the user
 getScoreList:  this is used to generated a sorted list of the score
 stockDetails: a object that keep track of all the
 * @constructor
 */
// @ts-ignore
function UserView(props: {
    email: string,
    faveList: string[],
    sortedScore: any,
    getFaveList: (value: string) => void,
    getScoreList: (value: string) => void,
    stockDetails: any,
}) {
    document.body.style.background = "#EFEDE9";
    const [stock, setStockName] = useState("Netflix Inc")
    const [displayScore, setDisplayScore] = useState("100")
    const [doWellIn, setDoWellIn] = useState("Deforestations,Fossil Fuel Use,Gender Equality,Prisons,Weapon,")
    const [displayPrice, setDisplayPrice] = useState("187.5")
    const [peratio, setperatio] = useState("16")
    const [marketCap, setMarketCap] = useState("84B")
    const [yields, setYields] = useState("null")

    return (
        <div>
            <Topbar/>
            <div className={"container"}>
                <Filters getSortedScore={props.getScoreList}/>
                <Boxes email={props.email}
                       setStockName={setStockName}
                       getFaveList={props.getFaveList}
                       sortedScore={props.sortedScore}
                       setDisplayScore={setDisplayScore}
                       setDisplayPrice={setDisplayPrice}
                       setYeilds={setYields}
                       setPeratio={setperatio}
                       setMarketCap={setMarketCap}
                       faveList={props.faveList}
                       setDoWellIn={setDoWellIn}
                       stockInformations={props.stockDetails}/>
                <Company score={displayScore}
                         stockname={stock}
                         pictureUrl={""}
                         stockDiscription={[]}
                         doWellIn={doWellIn}
                         peratio={peratio}
                         yields={yields}
                         marketCap={marketCap}
                         dprice={displayPrice}/>
            </div>
        </div>
    )
}

export default UserView;