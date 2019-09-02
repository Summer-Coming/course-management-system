/**
 * 初始化发送通知详情对话框
 */
var CmNotice2InfoDlg = {
    cmNotice2InfoData : {},
    editor: null,
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        },
        citySel: {
            validators: {
                notEmpty: {
                    message: '部门不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
CmNotice2InfoDlg.clearData = function() {
    this.cmNotice2InfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CmNotice2InfoDlg.set = function(key, val) {
    this.cmNotice2InfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CmNotice2InfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CmNotice2InfoDlg.close = function() {
    parent.layer.close(window.parent.CmNotice2.layerIndex);
}

/**
 * 收集数据
 */
CmNotice2InfoDlg.collectData = function() {
    this.CmNotice2InfoDlg['content'] = CmNotice2InfoDlg.editor.txt.html();
    this.set('deptid').set('id').set('title');
}

/**
 * 验证数据是否为空
 */
CmNotice2InfoDlg.validate = function () {
    $('#noticeInfoForm').data("bootstrapValidator").resetForm();
    $('#noticeInfoForm').bootstrapValidator('validate');
    return $("#noticeInfoForm").data('bootstrapValidator').isValid();
};


/**
 * 提交添加
 */
CmNotice2InfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/cmNotice2/add", function(data){
        Feng.success("添加成功!");
        window.parent.CmNotice2.table.refresh();
        CmNotice2InfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cmNotice2InfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CmNotice2InfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/cmNotice2/update", function(data){
        Feng.success("修改成功!");
        window.parent.CmNotice2.table.refresh();
        CmNotice2InfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cmNotice2InfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("noticeInfoForm", CmNotice2InfoDlg.validateFields);

    //初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.create();
    editor.txt.html($("#contentVal").val());
    CmNotice2InfoDlg.editor = editor;
});
