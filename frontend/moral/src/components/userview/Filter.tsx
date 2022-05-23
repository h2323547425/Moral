import React from 'react';
// @ts-ignore
import ReactSlider from 'react-slider';
import "./user.css";

/**
 * This is a class that generate one filter
 * @param props
 filterName: the name of this filter, (tere are in total five)
 filterValue: the values this filter is set on
 setFilterValue: (value: any) => void
 * @constructor
 */
function Filter(props: {
    filterName: string,
    filterValue: any,
    setFilterValue: (value: any) => void
}) {
    // we use a class library here for the filters
    return (
        <div id={props.filterName}>
            <p> {props.filterName}</p>
            <ReactSlider
                min={0}
                max={10}
                defaultValue={props.filterValue}
                onChange={state => props.setFilterValue(state)}
                className="horizontal-slider"
                thumbClassName="example-thumb"
                trackClassName="example-track"
            />
        </div>
    )

}

export default Filter