document.addEventListener('DOMContentLoaded',()=>{
 const area=document.getElementById('content'),count=document.getElementById('charCount');
 const clear=document.getElementById('clearBtn'),form=document.getElementById('debugForm'),submit=document.getElementById('analyzeBtn');
 const samples={
 java:`Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "name" is null\n    at com.example.demo.UserService.getName(UserService.java:24)\n    at com.example.demo.UserController.profile(UserController.java:18)\n\nPlease explain the root cause and show a safe fix.`,
 spring:`org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.demo.PaymentService' available\n\nThis happens when my Spring Boot application starts. Help me find why the bean is not detected.`,
 sql:`SQL Error: Column 'email' cannot be null\nINSERT INTO users (name, email) VALUES ('Asha', NULL);\n\nHow should I fix this in my Spring Data JPA application?`
 };
 const update=()=>{if(count)count.textContent=area.value.length.toLocaleString()+' / 20,000';};
 area?.addEventListener('input',update);
 clear?.addEventListener('click',()=>{area.value='';update();area.focus();});
 document.querySelectorAll('[data-sample]').forEach(btn=>btn.addEventListener('click',()=>{area.value=samples[btn.dataset.sample]||'';update();area.focus();}));
 form?.addEventListener('submit',e=>{if(!area.value.trim()){e.preventDefault();area.focus();return;}submit.disabled=true;submit.innerHTML='<span class="spinner"></span> Analyzing your issue...';});
 update();
});
