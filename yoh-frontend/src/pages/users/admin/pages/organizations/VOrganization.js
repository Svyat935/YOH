import {SearchFrame} from "../../../../../frame/SearchFrame/SearchFrame";
import React, {useState} from "react";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";
import {InfoLine} from "../../../../../components/infoLine/InfoLine";
import {ButtonA} from "../../../../../components/buttons/ButtonA/ButtonA";
import {InputPhone} from "../../../../../components/inputPhone/InputPhone";
import {ButtonB} from "../../../../../components/buttons/ButtonB/ButtonB";

export function VOrganization(props) {
    let filterList = [
        {
            "text": "По алфавиту", "value": 1, "onClick": () => {
            }
        },
        {
            "text": "По дате", "value": 2, "onClick": () => {
            }
        },
    ]
    const [show, setShow] = useState(false);

    const addOrganization = async () => {
        let validStatus = true;
        let fName = document.getElementById("name"),
            fEmail = document.getElementById("email"),
            fPhone = document.getElementById("phone"),
            fAddress = document.getElementById("address"),
            fWebsite = document.getElementById("website");
        let vEmail = document.getElementById("email-validate"),
            vPhone = document.getElementById("phone-validete");
        const validStyle = "1px red solid";

        fName.style.border = "none";

        if (!fName.value) {
            fName.style.border = validStyle;
            validStatus = false;
        }

        if (validStatus) {
            let body = {};
            body["name"] = fName.value;
            if (fEmail.value) body["email"] = fEmail.value;
            if (fPhone.value) body["phone"] = fPhone.value;
            if (fAddress.value) body["address"] = fAddress.value;
            if (fWebsite.value) body["website"] = fWebsite.value;

            let response = await props.addOrganization(body);

            if (response.code === 401){
                console.log(response);
                validStatus = false;
            }
        }

        if (validStatus){
            props.refresh();
            setShow(false);
        }
    }

    const createOrganizationsView = () => {
        let orgs = props.organizations.slice(0, 9),
            view = [];

        for (let org of orgs) {
            view.push(<InfoLine infoLeft={<b>Название организации: </b>} infoRight={org["name"]}/>);
        }
        return view;
    }

    const modalBody = (
        <div style={
            {
                display: "flex",
                flexDirection: "column",
                padding: "0 10px"
            }
        }>
            <label>Название организации: </label>
            <input id={"name"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required/>

            <label>Электронная почта: </label>
            <input id={"email"} type={"email"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required/>
            <p id={"email-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>

            <label>Телефон: </label>
            <InputPhone id={"phone"}/>
            <p id={"phone-validate"} style={{height: "5px", marginBottom: 0, color: "#800000"}}/>

            <label>Адрес: </label>
            <input id={"address"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required/>

            <label>Веб-сайт: </label>
            <input id={"website"} type={"text"} style={
                {borderRadius: 40, border: "none", padding: "5px 15px", marginBottom: 10}
            } required/>
        </div>
    )

    const modalFooter = (
        <>
            <ButtonB text={"Отмена"} onClick={() => setShow(false)}/>
            <ButtonB text={"Добавить"} onClick={addOrganization}/>
        </>
    )

    const buttons = (
        <ButtonA width={300} text={"Добавить +"} onClick={() => {
            setShow(true);
        }}/>
    )

    const paginationButtons = () => {
        let view = [];
        if (props.start) {
            view.push(<ButtonA width={300} text={"Предыдущая страница"} onClick={() => {
                props.setStart(props.start - 9);
                props.refresh();
            }}/>)
        }
        if (props.organizations.length === 10) {
            view.push(<ButtonA width={300} text={"Следующая страница"} onClick={() => {
                props.setStart(props.start + 9);
                props.refresh();
            }}/>)
        }
        return view;
    }

    const searchOrganizations = () => {
        let searchValue = document.getElementById("searchInput").value;
        props.setRegex(searchValue);
        props.refresh();
    }

    return <SearchFrame
        navPanel={<AdminNav context={props.context}/>}
        filterList={filterList}
        modalShow={show}
        modalOnHide={() => setShow(false)}
        modalTitle={"Добавить организацию"}
        modalBody={modalBody}
        modalFooter={modalFooter}
        title={"Список организаций: "}
        buttons={buttons}
        searchInputOnKeyDown={searchOrganizations}
        searchInputOnBlur={searchOrganizations}
        searchInputOnClick={searchOrganizations}
        content={createOrganizationsView()}
        contentFooter={paginationButtons()}
    />
}