import React from "react";
import "./SortBlock.css";

export function SortBlock(props) {
    const createFilterList = () => {
        let filterList = props.sorts,
            view = [];

        if (filterList !== undefined){
            filterList.forEach((filterOne) => {
                view.push(
                    <div key={filterOne["text"]} className={"sort-one"}>
                        <input
                            id={filterOne["text"]}
                            name={"filterName"}
                            type={"radio"}
                            value={filterOne["value"]}
                            onClick={filterOne["onClick"]}
                            defaultChecked={filterOne["defaultChecked"]}
                        />
                        <label htmlFor={filterOne["text"]}>{filterOne["text"]} {filterOne["icon"]}</label>
                    </div>
                )
            })
        }
        return view;
    }

    return (
        <div className={"sort-block"}>
            <h3>Сортировка</h3>
            <div className={"sort-list"}>
                {createFilterList()}
            </div>
        </div>
    )
}