__author__ = 'vinny.ly'
import sys
import os
from subprocess import call
from datetime import datetime

def generate_json(directory):
  now = datetime.now()
  print str(now)
  print "directory: " + directory

  if not directory.endswith('/'):  directory = directory + "/"
  xml_directory = os.path.abspath(directory + "xml/")
  json_directory = os.path.abspath(directory + "json/")
  print "converting files in xml directory: " + xml_directory
  print "outputting files to json directory: " + json_directory
  if not os.path.exists(json_directory):
    os.makedirs(json_directory)

  xml_files = next(os.walk(xml_directory))[2]
  for xml_filename in xml_files:
    json_filename = xml_filename.replace(".xml", ".json")
    print xml_filename + " > " + json_filename
    sh_command = "./xml2json.sh " + xml_directory + "/" + xml_filename + " > " + json_directory + "/" + json_filename
    os.system(sh_command)

if __name__=='__main__':
  if len(sys.argv) != 2:
    print "Usage : python "+ __file__ + " feed_directory"
    print "Example:"
    print "  python "+ __file__ + " ../../../message_samples/backup/"
  else:
    directory = sys.argv[1]
    generate_json(directory)
