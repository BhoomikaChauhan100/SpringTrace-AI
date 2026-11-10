document.addEventListener('DOMContentLoaded',()=>{
 const b=document.getElementById('copyResult');
 if(b)b.addEventListener('click',async()=>{
  try{await navigator.clipboard.writeText(document.getElementById('analysisText')?.innerText||'');b.textContent='Copied ✓';}
  catch{b.textContent='Copy unavailable';}
  setTimeout(()=>b.textContent='Copy report',1800);
 });
});
