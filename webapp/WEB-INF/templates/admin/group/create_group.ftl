
<#assign boxtitle>#i18n{portal.group.create_group.boxTitle}</#assign>
<@row>
    <@columns>
        <@box color='success'>
            <@boxHeader title=boxtitle />
            <@boxBody>
                <@tform name='create_group' action='jsp/admin/group/DoCreateGroup.jsp'>
                    <@formGroup labelFor='group_key' labelKey='${i18n("portal.group.create_group.labelName")}' mandatory=true rows=2>
                        <@input type='text' name='group_key' id='group_key' />
                    </@formGroup>  
                    <@formGroup labelFor='group_description' labelKey='${i18n("portal.group.modify_group.labelDescription")}' mandatory=true rows=2>
                        <@input type='text' name='group_description' id='group_description' />
					</@formGroup>
                    <@formGroup showLabel=false >  
                        <@columns offsetSm=3 sm=9 >
                            <@button type='submit' title='${i18n("portal.group.modify_group.buttonLabelValidate")}' />
                            <@button  title='${i18n("portal.group.modify_group.buttonCancel")}' params='onclick="javascript:history.go(-1)"' />
                        </@columns>	
                    </@formGroup>	
                </@tform>
             </@boxBody>
        </@box>
    </@columns>
</@row>   