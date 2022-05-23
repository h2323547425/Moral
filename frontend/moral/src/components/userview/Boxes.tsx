import React from 'react';
// @ts-ignore
import Box from './Box.tsx'

/**
 * This class is to generate all the boxes of stock which can be seen at the middle part
 email: the email for the user
 setStockName: (value: string) => void,
 stockInformations: the detailed information for all the stocks
 setDisplayScore: (value: string) => void,
 setDisplayPrice: (value: string) => void,
 setDoWellIn: (value: string) => void,
 faveList: the list of abbrev that the user likes
 sortedScore: a map like object of the sorted scores
 getFaveList: (value: string) => void
 setPeratio: (value: string) => void,
 setMarketCap: (value: string) => void,
 setYeilds: (value: string) => void,
 * @param props
 * @constructor
 */
function Boxes(props: {
    email: string,
    setStockName: (value: string) => void,
    stockInformations: any,
    setDisplayScore: (value: string) => void,
    setDisplayPrice: (value: string) => void,
    setDoWellIn: (value: string) => void,
    setPeratio: (value: string) => void,
    setMarketCap: (value: string) => void,
    setYeilds: (value: string) => void,
    faveList: string[],
    sortedScore: any,
    getFaveList: (value: string) => void
}) {

    return (
        <div className="stock-list-block">
            <h1 className={"font-title"}>Suggested Stocks</h1>
            <div className="scrollable">
                {Object.keys(props.sortedScore).map((cell) =>
                    <Box email={props.email} stockName={props.stockInformations[cell]["name"]}
                         score={props.sortedScore[cell]} price={props.stockInformations[cell]["Price"]}
                         abbrev={cell}
                         doWellIn={props.stockInformations[cell]["Fields_Does_Well"]}
                         yields={props.stockInformations[cell]["dividend yield"]}
                         marketCap={props.stockInformations[cell]["Market_Cap"]}
                         peratio={props.stockInformations[cell]["PE_Ratio"]}
                         setDoWellIn={props.setDoWellIn}
                         setDisplayScore={props.setDisplayScore} setStockName={props.setStockName}
                         faveList={props.faveList}
                         getFaveList={props.getFaveList}
                         setDisplayPrice={props.setDisplayPrice}
                    setMarketCap={props.setMarketCap}
                    setPeratio={props.setPeratio}
                    setYeilds={props.setYeilds}/>)}
            </div>
        </div>
    )

}

export default Boxes
