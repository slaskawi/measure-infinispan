while true ; do
  echo "Time " 
  date
  echo "Number of threads: "
  ps uH p $1 | wc -l

  echo "GC Stats: "
  jstat -gccause $1
  
  echo "Flags:"
  jcmd $1 VM.flags

  dstat -cmsa 10 2
  echo "------------------------------"
  sleep 5
done


