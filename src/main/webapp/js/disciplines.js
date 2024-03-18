const readDisciplines = () =>{
    xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.status === 200 && xhr.readyState === 4 ){
            const response = JSON.parse(xhr.responseText);
            if ( !response.status ) return;
            const data = JSON.parse(response.content);
            const container = document.getElementById("main");
            container.innerHTML = "";
            data.forEach( item => {
               container.appendChild(renderDiscipline(item.id, item.name, item.description, item.inGroup, item.participants));
            });
        }
    }

    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/discipline-servlet");
    xhr.send(null);
}

const renderDiscipline = (id, name, description, inGroup, participants) => {
    const container = document.createElement("div");
    container.setAttribute("class", "container-fluid mb-5");

    const titleContent = document.createElement("div");
    titleContent.setAttribute("class", "d-flex mb-3");
    const title = document.createElement("h3");
    title.setAttribute("class", "me-auto p-2");
    title.appendChild(document.createTextNode(name));

    const buttonEdit = document.createElement("button");
    const buttonAdd = document.createElement("button");
    const buttonDelete = document.createElement("button");
    buttonEdit.setAttribute("class","btn btn-primary");
    buttonAdd.setAttribute("class","btn btn-primary");
    buttonDelete.setAttribute("class","btn btn-primary");

    buttonEdit.appendChild(document.createTextNode("Edit"));
    buttonDelete.appendChild(document.createTextNode("Delete"));

    buttonAdd.appendChild(document.createTextNode("Add Participant"));

    //Add event listeners to add,edit and delete

    const code = document.createElement("p");
    code.appendChild(document.createTextNode(`code: ${id}`));

    const descriptionP = document.createElement("p");
    descriptionP.appendChild(document.createTextNode(`description: ${description}`));


    const groupType = document.createElement("p");
    groupType.appendChild(document.createTextNode(`in Group: ${inGroup ? "Yes": "No"}`));

    const array = JSON.parse(participants);
    let table;
    if (array.length === 0){
        table = document.createElement("br");
    }else {
        table = renderParticipants( array );
    }

    const divider = document.createElement("hr");
    divider.setAttribute("class", "hr-blurry");

    titleContent.appendChild(title);
    titleContent.appendChild(buttonEdit);
    titleContent.appendChild(buttonDelete);

    container.appendChild(titleContent);
    container.appendChild(code);
    container.appendChild(descriptionP);
    container.appendChild(groupType);
    container.appendChild(buttonAdd);
    container.appendChild(table);
    container.appendChild(divider);
    return container;
}

const renderParticipants = ( participants ) => {
    const table = document.createElement("table");
    table.setAttribute("class", "table table-striped-columns");

    const headerRow = document.createElement("thead");
    const titles = ["id", "name", "mail", "remove"];
    titles.forEach( attribute => {
       const td = document.createElement("td");
       td.appendChild(document.createTextNode(attribute));
       td.setAttribute("scope","col");
       headerRow.appendChild(td);
    });

    const tbody = document.createElement("tbody");
    participants.forEach( data => {
        const row = document.createElement("tr");
        titles.forEach( attribute => {
           const td = document.createElement("td");
           if (attribute === "remove"){
               const removebutton = document.createElement("button");
               removebutton.appendChild(document.createTextNode("remove"));
               removebutton.setAttribute("type", "button");
               removebutton.setAttribute("class", "btn btn-primary");

               td.appendChild(removebutton);


               //add listener


           }else {
               td.appendChild(document.createTextNode(data[attribute]));
           }
           row.appendChild(td);
        });
        tbody.appendChild(row);
    });
    table.appendChild(headerRow);
    table.appendChild(tbody);

    return table;
}


readDisciplines();