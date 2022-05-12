qucick_command = [
    {name: "治疗全部角色", command: "heal", args: []},
    {name: "获取当前位置", command: "position", args: []},
    {
        name: "给予摩拉", command: "give 202", args: [
            {type: "number", default: 100000, width: 145}
        ]
    },
    {
        name: "给予原石", command: "give 201", args: [
            {type: "number", default: 10000, width: 120}
        ]
    },
    {
        name: "给予纠缠之缘", command: "give 223", args: [
            {type: "number", default: 10000, width: 100}
        ]
    },
    {
        name: "给予相遇之缘", command: "give 224", args: [
            {type: "number", default: 10000, width: 100}
        ]
    },
    {name: "开启/关闭无敌模式", command: "godmode", args: []},
    {
        name: "设置世界等级(需要重新登陆)", command: "setworldlevel", args: [
            {type: "number", default: 8, width: 60}
        ]
    },
    {name: "获取全部道具(慎用，会变得没意思)", command: "giveall", args: []},
    {name: "清空背包", command: "clear all", args: []},
    {
        name: "设置当前角色E技能等级", command: "talent e", args: [
            {type: "number", default: 10, width: 60}
        ]
    },
    {
        name: "设置当前角色Q技能等级", command: "talent q", args: [
            {type: "number", default: 10, width: 60}

        ]
    },
    {
        name: "设置当前角色普攻等级", command: "talent n", args: [
            {type: "number", default: 10, width: 60}
        ]
    },
    {name: "清空材料", command: "clear mat", args: []},
    {name: "清空圣物", command: "clear art", args: []},
    {name: "清除当前角色命之座(需要重新登陆)", command: "resetconst", args: []},
    {name: "清除全部角色命之座(需要重新登陆)", command: "resetconst all", args: []},
    {name: "自杀", command: "killcharacter", args: []},
]

function genQuickCommand() {
    const panel = document.getElementById("panel");
    panel.innerHTML = "<div class=\"mui-card-header\">常用命令(对自己角色操作)</div>\n" +
        "<ul class=\"mui-table-view\" id=\"cmd_list\"></ul>";
    const cmd_list = document.getElementById("cmd_list");
    let i;
    let arg = 0;
    for (i = 0; i < qucick_command.length; i++) {
        const command = qucick_command[i];
        const div = document.createElement("li");
        const label = document.createElement("span");
        const div2 = document.createElement("div");
        const button = document.createElement("button");
        const hiddenCommand = document.createElement("input");

        div.classList.add('mui-table-view-cell');
        div2.classList.add('mui-input-row');
        button.classList.add('mui-btn');
        button.classList.add('mui-btn-royal');
        label.innerText = command.name;
        button.innerText = "执行";
        div.appendChild(label);
        div.appendChild(div2);

        hiddenCommand.value = command.command;
        hiddenCommand.style.display = "none";
        div2.appendChild(hiddenCommand);
        for (arg = 0; arg < command.args.length; arg++) {
            const arg_item = command.args[arg];
            switch (arg_item.type) {
                case "number":
                    const input = document.createElement('input');
                    input.type = arg_item.type;
                    input.value = arg_item.default;
                    if (arg_item.width) {
                        input.style.width = arg_item.width + 'px';
                    }
                    div2.appendChild(input);
            }
        }


        div2.appendChild(button);
        cmd_list.appendChild(div);
        button.onclick = (e) => {
            const parent = e.target.parentNode;
            let payload = "";
            let first = true;
            for (let child = parent.firstChild; child !== null; child = child.nextSibling) {
                if (child.tagName !== "INPUT") continue;
                if (!first) {
                    payload += " ";
                }
                first = false;
                payload += child.value;
            }
            sendCommand(payload);
        }
    }
}