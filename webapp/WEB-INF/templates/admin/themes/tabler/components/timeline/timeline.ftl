<#-- 
Macro: timeline

Description: Generates an HTML <ul> element to display a timeline, with an optional class and ID.

Parameters:
- class (string, optional): additional classes to add to the HTML code.
- id (string, optional): the ID of the <ul> element containing the timeline.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic timeline:

    <@timeline>
        <@timelineItem label='Task created' time='10:00 AM' iconFace='plus' iconBg='bg-primary'>
            A new task has been created.
        </@timelineItem>
        <@timelineItem label='Task completed' time='02:30 PM' iconFace='check' iconBg='bg-success'>
            The task has been completed.
        </@timelineItem>
    </@timeline>

    Timeline with custom ID and class:

    <@timeline id='activityLog' class='mt-3'>
        <@timelineItem label='User login' time='09:00 AM' iconFace='user' iconBg='bg-info'>
            User logged into the system.
        </@timelineItem>
    </@timeline>

-->
<#macro timeline class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="list list-timeline<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#nested>
</ul>
</#macro>