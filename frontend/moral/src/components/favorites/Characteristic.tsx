import React from 'react';

/**
 * Component that represents a characteristic of a stock
 * @param props - feature, value
 * @return a characteristic of a stock
 */
function Characteristic(props: {
    feature: string,
    value: string}) {

    return (
        <div>
            <div className={"combined"} id="price">
                <div className={"feature"}> {props.feature}</div>
                <div className={"value"}> {props.value}</div>
            </div>
        </div>
    )

}

export default Characteristic;