package com.sb.atlas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sb.atlas.excel.BeanToExcel;
import com.sb.atlas.excel.ExcelColumns;
import com.sb.atlas.excel.FormatType;
import com.sb.atlas.model.GroupEntity;
import com.sb.atlas.model.Role;
import com.sb.atlas.service.GroupEntityService;
/**
 * @author mahesh chand
 * @since 01-Sep-2014 
 */
/**
 * @author mahesh chand
 * @since 01-Sep-2014 
 */
@Controller
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GroupEntityController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GroupEntityController.class);

	@Wire
	private Window searchGroupEntityWin;
	@Wire
	private Textbox businessEntity;
	@Wire
	private Textbox roleCreationRoleDescription;
	
	@Wire 
	private Groupbox lstUserGroupBox;
	@WireVariable
	GroupEntityService groupEntityService;
	
	@Wire 
	private Listbox businessEntityListbox;
	
	
	
	@RequestMapping(value = "/groupEntity", method = RequestMethod.GET)
	public ModelAndView redirectToCreatePage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("@@ redirectToCreatePage ..");
		return new ModelAndView("groupEntity");
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
	}


	@Listen("onClick = #roleCreationBtnCancel")
	public void closeResetPwdForm(Event e) {
		searchGroupEntityWin.detach();
	}



	@Listen("onClick = #roleCreationBtnSubmit")
	public void resetPassword(Event e) {
		
		Role role=new Role();
	//	role.setRoleName(roleCreationRoleName.getText());
		role.setRoleDesc(roleCreationRoleDescription.getText());
		//roleService.create(role);
		
	}
	
	@Listen("onClick = button#btnSearch")    
	public void SearchUser() 
	{		
		  logger.debug("**** SearchUser:: ");
		  GroupEntity  groupEntity=new GroupEntity();
		  groupEntity.setGroupEntity(businessEntity.getText());
		  List<GroupEntity> result = groupEntityService.search(groupEntity);
		  lstUserGroupBox.setVisible(true);
		  businessEntityListbox.setModel(new ListModelList<GroupEntity>(result));		  
		  businessEntityListbox.setItemRenderer(new GrpEntityItemRenderer());
		  logger.debug("**** SearchUser:: result.size::"+result.size());
	}	
	
	@Listen("onClick = button#btnDelete")
	public void deleteUser() {
		logger.debug("**** deleteUser :: btnDelete clicked...");
		try 
		{	
			logger.debug("Selected count : "+businessEntityListbox.getSelectedCount());
			
			if(businessEntityListbox.getSelectedCount()==0){
				Messagebox.show(Labels.getLabel("brd.hierarchy.delete.notselected"), "Information",Messagebox.OK,Messagebox.INFORMATION);
				Messagebox.show(Labels.getLabel("businessEntityListbox.Group Entity.col"), "Information",Messagebox.OK,Messagebox.INFORMATION);
			}
			
			logger.debug("businessEntityListbox.getSelectedItem().getValue() : "+businessEntityListbox.getSelectedItem().getValue());
			GroupEntity groupEntity=businessEntityListbox.getSelectedItem().getValue();	
			businessEntityListbox.removeItemAt(businessEntityListbox.getSelectedIndex());
			groupEntity.setEnabled(false);
			groupEntityService.save(groupEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Listen("onClick = button#btnDownload")
	public void downloadGroupEntity() throws IOException {

		 GroupEntity  groupEntity=new GroupEntity();
		  groupEntity.setGroupEntity(businessEntity.getText());
		  List<GroupEntity> result = groupEntityService.search(groupEntity);
		  export_to_excel(result,businessEntityListbox);
	}
	public  void export_to_csv(Listbox listbox) {
		String s = ";";
		StringBuffer sb = new StringBuffer();

		for (Object head : listbox.getHeads()) {
			String h = "";
			for (Object header : ((Listhead) head).getChildren()) {
				h += ((Listheader) header).getLabel() + s;
			}
			sb.append(h + "\n");
		}
		for (Object item : listbox.getItems()) {
			String i = "";
			for (Object cell : ((Listitem) item).getChildren()) {
				i += ((Listcell) cell).getLabel() + s;
			}
			sb.append(i + "\n");
		}
		Filedownload.save(sb.toString().getBytes(), "text/plain", "test.csv");

	}
	
	
private static  void export_to_excel(List<GroupEntity> dataList,Listbox listbox) throws IOException {
	logger.debug("listbox.getHeads()  :"+listbox.getHeads());
	List<ExcelColumns> excelColumns = new ArrayList<ExcelColumns>();
	for (Object head : listbox.getHeads()) {
        for (Object header : ((Listhead) head).getChildren()) {
        	String label=((Listheader) header).getLabel();
        	logger.debug("((Listheader) header)"+((Listheader) header).getLabel());
        	logger.debug("((Listheader) header)"+((Listheader) header).getValue());
        	excelColumns.add(new ExcelColumns(label, Labels.getLabel("businessEntityListbox.Group Entity.col").trim(),FormatType.TEXT));
        	
        }
      }
	
	/*
	excelColumns.add(new ExcelColumns("groupEntityId", "ID",FormatType.LONG));
	excelColumns.add(new ExcelColumns("groupEntity", "Group Entity",FormatType.TEXT));*/
	BeanToExcel beanToExcel = new BeanToExcel();
	beanToExcel.setExcelColumns(excelColumns);
	beanToExcel.setDataSheetName("Test");
	beanToExcel.setDataList(dataList);
	beanToExcel.exportToExcel();
}
	
	
	class GrpEntityItemRenderer implements ListitemRenderer<Object>
	{
		@Override
		public void render (Listitem listitem, Object value, int index) {
			GroupEntity GroupEntity = (GroupEntity)value;
			listitem.setValue(value);
			listitem.appendChild(new Listcell(GroupEntity.getGroupEntityId().toString()));
			listitem.appendChild(new Listcell(GroupEntity.getGroupEntity()));
		}	
	}

}
