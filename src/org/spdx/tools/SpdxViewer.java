/**
 * Copyright (c) 2010 Source Auditor Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.spdx.tools;

import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocumentFactory;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.tag.CommonCode;

/**
 * Simple pretty printer for SPDX RDF XML files. Writes output to System.out.
 * Usage: PrettyPrinter SPDXRdfXMLFile > textFile where SPDXRdfXMLFile is a
 * valid SPDX RDF XML file
 *
 * @author Gary O'Neall
 * @version 0.1
 */

public class SpdxViewer {

	static final int MIN_ARGS = 1;
	static final int MAX_ARGS = 1;

	/**
	 * Pretty Printer for an SPDX Document
	 *
	 * @param args
	 *            Argument 0 is a the file path name of the SPDX RDF/XML file
	 */

	public static void main(String[] args) {
		if (args.length < MIN_ARGS) {
			System.console()
					.printf("Usage:\n SPDXViewer file\nwhere file is the file path to a valid SPDX RDF XML file");
			return;
		}
		if (args.length > MAX_ARGS) {
			System.out.printf("Warning: Extra arguments will be ignored");
		}
		SpdxDocument doc = null;
		try {
			doc = SPDXDocumentFactory.createSpdxDocument(args[0]);
		} catch (Exception ex) {
			System.out
					.print("Error creating SPDX Document: " + ex.getMessage());
			return;
		}
		PrintWriter writer = new PrintWriter(System.out);
		try {
			List<String> verify = doc.verify();
			if (verify.size() > 0) {
				System.out.println("This SPDX Document is not valid due to:");
				for (int i = 0; i < verify.size(); i++) {
					System.out.print("\t" + verify.get(i)+"\n");
				}
			}
			// read the constants from a file
			Properties constants = CommonCode
					.getTextFromProperties("org/spdx/tag/SpdxViewerConstants.properties");
			// print document to system output using human readable format
			CommonCode.printDoc(doc, writer, constants);
		} catch (InvalidSPDXAnalysisException e) {
			System.out.print("Error pretty printing SPDX Document: "
					+ e.getMessage());
			return;
		} catch (Exception e) {
			System.out.print("Unexpected error displaying SPDX Document: "
					+ e.getMessage());
		} finally {
			writer.close();
		}
	}
}
