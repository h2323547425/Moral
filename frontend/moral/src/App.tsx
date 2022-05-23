import React, {useState} from 'react';
import './App.css';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import AboutPage from "./pages/AboutPage";
import FavoritesPage from "./pages/FavoritesPage";
import UserView from "./components/userview/UserView";

function App() {
    const [email, setEmail] = useState("");
    const [faveList, setFaveList] = useState([] as string[])
    const [stockDetails, setStockDetails] = useState({} as any)
    const [sortedScore, setSortedScore] = useState({} as any);

    const getFaveList = async (email: string) => {
        let URL = "http://localhost:4567/favorites?username=" + email;
        const requestOptions = {
            method: 'GET',
            headers: {"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
        };
        const response = await fetch(URL, requestOptions)
        const body = await response.json()
        setFaveList(body["favorites"])
    }

    const getStockDetails = async () => {
        let URL = "http://localhost:4567/stockinfo"
        const requestOptions = {
            method: 'GET',
            headers: {
                'Access-Control-Allow-Origin': "*"
            }
        }
        const response = await fetch(URL, requestOptions)
        const body = await response.json()
        setStockDetails(body)
    }

    const getScoreList = async (filters: string) => {
        let URL = "http://localhost:4567/stockscores?filterValue=" + filters;
        const requestOptions = {
            method: 'GET',
            headers: {"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
        };
        const response = await fetch(URL, requestOptions);
        const body = await response.json();
        setSortedScore(body);
    }

    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage setEmail={setEmail}
                                                    getFaveList={getFaveList}
                                                    getStockDetails={getStockDetails}
                                                    getSortedList={getScoreList}/>}/>
                <Route path="/home"
                       element={<UserView email={email} getFaveList={getFaveList} faveList={faveList}
                                          stockDetails={stockDetails}
                                          getScoreList={getScoreList}
                                          sortedScore={sortedScore}/>}/>
                <Route path="/about" element={<AboutPage/>}/>
                <Route path="/favorites"
                       element={<FavoritesPage email={email} faveList={faveList} getFaveList={getFaveList}
                                               setFaveList={setFaveList}
                                               stockDetails={stockDetails}/>}/>
            </Routes>
        </Router>
    );

}

export default App;