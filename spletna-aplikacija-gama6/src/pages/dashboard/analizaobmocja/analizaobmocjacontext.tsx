/* disable-eslint */
import { createContext } from "react";
const AnalizaObmocjaContext = createContext({
    transactions : [],
    setTransactions : (transactions) => {},
    dateFrom : new Date(),
    setDateFrom : (dateFrom) => {},
    dateTo : new Date(),
    setDateTo : (dateTo) => {},
    selectedPolygon : [],
    setSelectedPolygon : (selectedPolygon) => {},
});


const AnalizaObmocjaProvider = ({children}) => {
    const [transactions, setTransactions] = useState([]);
    const [dateFrom, setDateFrom] = useState(new Date());
    const [dateTo, setDateTo] = useState(new Date());
    const [selectedPolygon, setSelectedPolygon] = useState([]);

    return (
        <AnalizaObmocjaContext.Provider value={{transactions, setTransactions, dateFrom, setDateFrom, dateTo, setDateTo, selectedPolygon, setSelectedPolygon}}>
            {children}
        </AnalizaObmocjaContext.Provider>
    )
}


