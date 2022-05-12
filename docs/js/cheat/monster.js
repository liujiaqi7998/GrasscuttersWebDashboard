var delayedSearch = null;

function genMonster() {
    var panel = document.getElementById("panel");
    panel.innerHTML = `
    <div class="mui-card-header">在自己周围生成怪物</div>
    <div class="mui-card-content">
        <ul class="mui-table-view">
            <div style="display: flex; flex-direction: column;">
            <li class="mui-table-view-cell">
                <label for="entity-search">怪物名称:</label>
                <div style="display: flex; align-items: center; overflow: hidden;">
                        <input id="entity-search" style="flex: 4" type="text" placeholder="搜索怪物名称" />
                        <button id="clear" class="mui-btn mui-btn-danger mui-btn-outlined" style="margin-left: 0.25em; transition: all ease-in-out 0.5s; flex: 0; opacity: 0;">清除</button>
                    </div>
                        <div id="name-list" style="background-color: #ecf0f1; overflow-y: auto; overflow-x: hidden; max-height: 10em;height: 100%; transition: all ease-in-out 0.5s;">
                    </div>
                 </div>
            </li>
            <li class="mui-table-view-cell"><label for="amount">数量:</label><input type="number" id="amount" name="amount" value=1 /></li>
            <li class="mui-table-view-cell"><label for="amount">等级(仅怪物适用):</label><input type="number" id="level" name="level" value=50 /></li>
            <li class="mui-table-view-cell"><input style="display: none" id="entity-id" /></li>
        </ul>
    </div>
    <div class="mui-card-footer">
            <button disabled id="execute">发送</button>
     </div>`;
    updateEntityList();

    document.getElementById("entity-search").oninput = (e) => {
        if (e.target.value.length > 0) {
            document.getElementById("clear").style.flex = 1;
            document.getElementById("clear").style.opacity = 1;
        } else {
            document.getElementById("clear").style.flex = 0;
            document.getElementById("clear").style.opacity = 0;
        }
        if (delayedSearch) {
            clearTimeout(delayedSearch);
        }
        delayedSearch = setTimeout(() => updateEntityList(), 500);
        document.getElementById("name-list").style.height = "10em";
    };
    document.getElementById("clear").onclick = () => {
        document.getElementById("entity-search").value = "";
        updateEntityList();
    }
    document.getElementById("entity-search").onkeydown = (e) => {
        if (e.key === "Escape") {
            document.getElementById("entity-search").value = "";
            updateEntityList();
        }
    }
    document.getElementById("entity-id").setvalue = (v) => {
        document.getElementById("entity-id").value = v;
        if (v) {
            document.getElementById("execute").disabled = false;
        } else {
            document.getElementById("execute").disabled = true;
        }
    }
    document.getElementById("name-list").onclick = (e) => {
        if (e.target.tagName === "INPUT") {
            const list = document.getElementById("name-list");
            list.style.height = "3em";
            const name = e.target.attributes['entity-name'].nodeValue ? e.target.attributes['entity-name'].nodeValue : "UNKNOWN";
            const color = {0: 'blue', 1: 'orange'}[e.target.attributes['entity-level'].nodeValue];
            list.innerHTML = `<input name="stack" type="radio" name="entity-id" entity-id="${e.target.attributes['entity-id'].nodeValue}" entity-name="${e.target.attributes['entity-name'].nodeValue}">
                <span class="button quality-${color}">
                ${name} - ${e.target.attributes['entity-id'].nodeValue}
                </span>`;
            document.getElementById("entity-search").value = name;
            document.getElementById("clear").style.flex = 1;
            document.getElementById("clear").style.opacity = 1;
            document.getElementById("entity-id").setvalue(e.target.attributes['entity-id'].nodeValue);

        }
    }
    // updateWeaponList();
    // document.getElementById("weapon-filter").onchange = updateWeaponList;
    document.getElementById("execute").onclick = () => {
        const entityId = document.getElementById("entity-id").value;
        const amount = document.getElementById("amount").value;
        const level = document.getElementById("level").value;
        sendCommand(`spawn ${entityId} ${amount} ${level}`);
    }
}

function updateEntityList() {
    const filter = document.getElementById("entity-search").value;
    const list = document.getElementById("name-list");
    list.innerHTML = "";
    list.style.height = "10em";
    monster_data.forEach(element => {
        if (filter === "" || element.name.toLowerCase().indexOf(filter.toLowerCase()) !== -1) {
            const o = document.createElement("label");
            o.style.marginLeft = "0.1em";
            const color = {0: 'blue', 1: 'orange'}[element.type];
            o.innerHTML = `<input name="stack" type="radio" name="entity-id" entity-id="${element.id}" entity-name="${element.name}" entity-level="${element.type}">
                <span class="button quality-${color}">
                ${element.name ? element.name : "UNKNOWN"}
                </span>`;
            list.appendChild(o);
        }

    });

}
