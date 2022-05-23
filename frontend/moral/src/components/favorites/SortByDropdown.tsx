import React, {useState} from 'react';
import 'react-dropdown/style.css';

/**
 * Component that represents the sort by dropdown
 * @param props - faveList, setFaveList, stockDetails, refreshFaves, setRefreshFaves
 * @return the sort by dropdown
 */
function SortByDropdown(props: {
    faveList: string[],
    setFaveList: (value: string[]) => void,
    stockDetails: any,
    refreshFaves: boolean,
    setRefreshFaves: (value: boolean) => void,
    setSortedList: (value: string[]) => void,
    sortedList: string[]
}) {

    const [sortBy, setSortBy] = useState("Recently Added")
    const [recentlyAdded, setRecentlyAdded] = useState(props.faveList.slice(0))
    const options = [
        'Recently Added',
        'Alphabetical (A-Z)',
        'Alphabetical (Z-A)',
        'Lowest to Highest Price',
        'Highest to Lowest Price']

    /**
     * Function that handles sorting the stocks by a specific method
     * @param event the change of the select value for the dropdown
     */
    const handleSortBy = (event: { target: { value: React.SetStateAction<string>; }; }) => {
        setSortBy(event.target.value);
        props.setRefreshFaves(!props.refreshFaves)
        switch (event.target.value) {
            case 'Recently Added':
                const filtered = recentlyAdded.filter(stock => props.faveList.some(other => other === stock))
                setRecentlyAdded(filtered)
                props.setSortedList(filtered)
                break
            case 'Alphabetical (A-Z)':
                const alphabetical_asc = props.faveList.sort((a, b) => a.localeCompare(b));
                props.setSortedList(alphabetical_asc)
                break
            case 'Alphabetical (Z-A)':
                const alphabetical_desc = props.faveList.sort((a, b) => b.localeCompare(a));
                props.setSortedList(alphabetical_desc)
                break
            case 'Lowest to Highest Price':
                const price_asc = props.faveList.slice(0).sort(function (a, b) {
                    if (parseFloat(props.stockDetails[a]["Price"]) > parseFloat(props.stockDetails[b]["Price"])) return 1;
                    else if (parseFloat(props.stockDetails[a]["Price"]) < parseInt(props.stockDetails[b]["Price"])) return -1;
                    else return 0;
                })
                props.setSortedList(price_asc)
                break
            case 'Highest to Lowest Price':
                const price_desc = props.faveList.slice(0).sort(function (a, b) {
                    if (parseFloat(props.stockDetails[b]["Price"]) > parseFloat(props.stockDetails[a]["Price"])) return 1;
                    else if (parseFloat(props.stockDetails[b]["Price"]) < parseInt(props.stockDetails[a]["Price"])) return -1;
                    else return 0;
                })
                props.setSortedList(price_desc)
                break;
            default:
                return
        }
    }

    return (
        <div className="sort-by-dropdown">
            <select value={sortBy} onChange={handleSortBy}>
                {options.map((option) => (
                    <option value={option} key={option}>{option}</option>
                ))}
            </select>
        </div>
    )

}

export default SortByDropdown