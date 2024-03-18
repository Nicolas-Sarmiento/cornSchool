document.addEventListener("DOMContentLoaded", () => {
    readDisciplines();
});

const readDisciplines = () =>{
    xhr = new XMLHttpRequest();

    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/discipline-servlet");
    xhr.onreadystatechange = () => {
        if (xhr.status === 200 && xhr.readyState === 4 ){
            const response = JSON.parse(xhr.responseText);
            console.log(response)
            if ( !response.status ) return;
            const data = JSON.parse(response.content);
            const container = document.getElementById("main");
            container.innerHTML = "";
            data.forEach( item => {
               container.appendChild(renderDiscipline(item.id, item.name, item.description, item.inGroup, item.participants));
            });
        }
    }

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

    buttonEdit.addEventListener("click", () => {
        openEditDisciplineModal(id, name, description, inGroup);
    });

    buttonDelete.addEventListener("click", () => {
        openDeleteDisciplineModal(id)
    })

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


document.querySelector("#addDisciplineButton").addEventListener("click", () => {

    const id = document.querySelector("#idDisciplineAdd").value;
    const name = document.querySelector("#nameDisciplineAdd").value;
    const description = document.querySelector("#descriptionDisciplineAdd").value;
    const inGroup = document.querySelector("#inGroupDisciplineAdd").value === "true";

    const data = {
        id: id,
        name: name,
        description: description,
        inGroup: inGroup
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/AddDiscipline", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    alert("Discipline added successfully.");


                    const modal = document.getElementById('addDisciplineModal');
                    const bootstrapModal = bootstrap.Modal.getInstance(modal);
                    bootstrapModal.hide();

                    document.querySelector("#idDisciplineAdd").value = "";
                    document.querySelector("#nameDisciplineAdd").value = "";
                    document.querySelector("#descriptionDisciplineAdd").value = "";
                    document.querySelector("#inGroupDisciplineAdd").value = "";


                } else {
                    alert(response.message);

                }
                readDisciplines();
            } else {
                alert("Error adding Discipline.");
            }
        }
    };

    xhr.send(JSON.stringify(data));
})

const openEditDisciplineModal = (id, name, description, inGroup) => {
    const modal = document.getElementById('updateDisciplinesModal');

    modal.querySelector("#idDisciplineEdit").value = id;
    modal.querySelector("#nameDisciplineEdit").value = name;
    modal.querySelector("#descriptionDisciplineEdit").value = description;
    modal.querySelector("#inGroupDisciplineEdit").value = inGroup ? "true" : "false";

    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
};

document.getElementById("editDisciplinesButton").addEventListener("click", () => {
    const id = document.getElementById("idDisciplineEdit").value;
    const name = document.getElementById("nameDisciplineEdit").value;
    const description = document.getElementById("descriptionDisciplineEdit").value;
    const inGroup = document.getElementById("inGroupDisciplineEdit").value === "true";

    const data = {
        id: id,
        name: name,
        description: description,
        inGroup: inGroup
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/EditDiscipline", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.message === "Discipline updated successfully") {
                    alert("Discipline updated successfully.");
                    readDisciplines();
                    const modal = new bootstrap.Modal(document.getElementById('deleteDescriptionModal'));
                    modal.hide();
                } else {
                    alert("Failed to update Discipline: " + response.message);
                }
            } else {
                alert("Error: " + xhr.status);
            }
        }
    };

    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(data));
});

const openDeleteDisciplineModal = (id) => {
    document.querySelector("#deleteDisciplineId").value = id;
    const modal = document.getElementById('deleteDescriptionModal');
    let bootstrapModal = bootstrap.Modal.getInstance(modal);
    if (!bootstrapModal) {
        bootstrapModal = new bootstrap.Modal(modal);
    }
    bootstrapModal.show();
}

document.querySelector("#deleteDisciplineButton").addEventListener("click", () => {
    const disciplineId = document.querySelector("#deleteDisciplineId").value;
    const data = {
        id: disciplineId
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/DeleteDiscipline", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.message === "Discipline deleted successfully") {
                    alert("Discipline deleted successfully.");
                   ;
                    const modal = document.getElementById('deleteDescriptionModal');
                    const bootstrapModal = new bootstrap.Modal(modal);
                    bootstrapModal.hide(); // Cerrar el modal aquí
                } else {
                    alert("Failed to delete Discipline: " + response.message);
                }
                readDisciplines();
            } else {
                alert("Error: " + xhr.status);
            }
        }
    };

    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(data));
});


