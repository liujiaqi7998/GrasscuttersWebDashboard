function genAvatar() {
    var panel = document.getElementById("panel");
    panel.innerHTML = `
        <div class="mui-card-header">向自己发送角色</div>
        <div class="mui-card-content">
            <ul class="mui-table-view">
                <li class="mui-table-view-cell">
                    <label for="character-id">角色名称:</label>
                    <div style="display: flex;">
                        <select id="character-filter" class="mui-btn mui-btn-block" style="flex: 2">
                            <option value="0"> 筛选：全部 </option>
                            <option value="Electric"> 筛选：雷元素 </option>
                            <option value="Ice"> 筛选：冰元素 </option>
                            <option value="Wind"> 筛选：风元素 </option>
                            <option value="Water"> 筛选：水元素 </option>
                            <option value="Fire"> 筛选：火元素 </option>
                            <option value="Rock"> 筛选：岩元素 </option>
                            <option value="Grass"> 筛选：草元素 </option>
                        </select>
                        <select id="character-id" class="mui-btn mui-btn-block" style="flex: 4; margin-left: 0.5em"> </select>
                    </div>
                </li>
                 <li class="mui-table-view-cell"><label for="level">等级:</label><input type="number" id="level" name="level" value=1 /></li>
                 <li class="mui-table-view-cell"> <label for="amount">命座（以命星的形式发送）:</label><input type="number" id="amount" name="amount" value=0 /></li>
            </ul>
        </div>
        <div class="mui-card-footer">
            <button id="execute">发送</button>
        </div>`;

    updateCharacterList();
    document.getElementById("character-filter").onchange = updateCharacterList;
    document.getElementById("execute").onclick = () => {
        var characterId = document.getElementById("character-id").value;
        var amount = document.getElementById("amount").value;
        var level = document.getElementById("level").value;
        if (characterId) {
            sendCommand(`givechar ${characterId} ${level}`);
            if (amount > 0) {
                sendCommand(`give ${characterId % 1000 + 1100} ${amount}`);
            }
        }
    }
}


function updateCharacterList() {
    var filter = document.getElementById("character-filter").value;
    var select = document.getElementById("character-id");
    select.innerHTML = "";
    console.log(filter);
    avatar_list.forEach(element => {
        if (filter == 0 || element.element == filter) {
            var o = document.createElement("option");
            o.innerText = `${element.name} - ${element.id}`;
            ;
            o.value = element.id;
            select.appendChild(o);
        }

    });

}
