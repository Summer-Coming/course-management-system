/**
 * PM管理管理初始化
 */
var CmPm = {
    id: "CmPmTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CmPm.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            // {title: '头像', field: 'avatar', visible: true, align: 'center', valign: 'middle'},
            {title: '账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            // {title: '密码', field: 'password', visible: true, align: 'center', valign: 'middle'},
            // {title: 'md5密码盐', field: 'salt', visible: true, align: 'center', valign: 'middle'},
            {title: '名字', field: 'name', visible: true, align: 'center', valign: 'middle'},
            // {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            // {title: '性别（1：男 2：女）', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '电子邮件', field: 'email', visible: true, align: 'center', valign: 'middle'},
            {title: '电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            // {title: '角色id', field: 'roleid', visible: true, align: 'center', valign: 'middle'},
            // {title: '部门id', field: 'deptid', visible: true, align: 'center', valign: 'middle'},
            // {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '部门', field: 'deptName', visible: true, align: 'center', valign: 'middle', sortable: true},
            {title: '状态', field: 'statusName', visible: true, align: 'center', valign: 'middle', sortable: true},
        // {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            // {title: '保留字段', field: 'version', visible: true, align: 'center', valign: 'middle'},
            {title: '课程编号', field: 'classId', visible: true, align: 'center', valign: 'middle'},
            {title: '结对小组', field: 'teamId', visible: true, align: 'center', valign: 'middle'},
            {title: '团队小组', field: 'groupId', visible: true, align: 'center', valign: 'middle'},
            {title: '对应助教账号', field: 'assistantId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
CmPm.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CmPm.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加PM管理
 */
CmPm.openAddCmPm = function () {
    var index = layer.open({
        type: 2,
        title: '添加PM管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cmPm/cmPm_add'
    });
    this.layerIndex = index;
};

/**
 * 点击批量添加PM管理
 */
CmPm.openBatchUploadCmPm = function () {
    var index = layer.open({
        type: 2,
        title: '批量添加PM管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cmPm/cmPm_batchUpload'
    });
    this.layerIndex = index;
};

/**
 * 打开查看PM管理详情
 */
CmPm.openCmPmDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'PM管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/cmPm/cmPm_update/' + CmPm.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除PM管理
 */
CmPm.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/cmPm/delete", function (data) {
            Feng.success("删除成功!");
            CmPm.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cmPmId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询PM管理列表
 */
CmPm.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    CmPm.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = CmPm.initColumn();
    var table = new BSTable(CmPm.id, "/cmPm/list", defaultColunms);
    table.setPaginationType("client");
    CmPm.table = table.init();
});
