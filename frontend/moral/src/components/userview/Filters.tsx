import React, {useState} from 'react';
// @ts-ignore
import Filter from "./Filter"
import "./user.css";

/**
 * This is a class that shape all of the filters in one block
 * @param props
 * getSortedScore: (value: string) => void: this is a function that generated the
 * new score based on the filter values
 * @constructor
 */
function Filters(props: {
    getSortedScore: (value: string) => void
}) {
    const [gender, setGender] = useState(5 as any)
    const [deforestation, setDeforestation] = useState(5 as any)
    const [weapon, setWeapon] = useState(5 as any)
    const [poison, setPoison] = useState(5 as any)
    const [fossil, setFossil] = useState(5 as any)

    const ngender = (gender / 10).toString()
    const ndeforestation = (deforestation / 10).toString()
    const nweapon = (weapon / 10).toString()
    const npoison = (poison / 10).toString()
    const nfossil = (fossil / 10).toString()

    const ratio = ngender + "," + ndeforestation + "," + nweapon + "," + npoison + "," + nfossil

    return (
        <div className="filter-block">
            <h1 className={"font-title"}> Filter</h1>
            <p> Use the sliding bars to denote the level of importance of the following categories</p>
            <button className={"font-title"}
                    onClick={() => props.getSortedScore(ratio)}>Recommend
            </button>
            <div><Filter filterName={"Deforestation Free"} filterValue={deforestation}
                         setFilterValue={setDeforestation}/></div>
            <div><Filter filterName={"Gender Equality"} filterValue={gender} setFilterValue={setGender}/></div>
            <div><Filter filterName={"Weapons Free"} filterValue={weapon} setFilterValue={setWeapon}/></div>
            <div><Filter filterName={"Prison Free"} filterValue={poison} setFilterValue={setPoison}/></div>
            <div><Filter filterName={"Fossil Free"} filterValue={fossil} setFilterValue={setFossil}/></div>
        </div>
    )
}

export default Filters