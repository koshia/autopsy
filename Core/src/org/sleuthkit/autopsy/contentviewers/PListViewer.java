/*
 * Autopsy Forensic Browser
 *
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.contentviewers;

import java.awt.Component;
import java.util.List;
import org.sleuthkit.datamodel.AbstractFile;
import java.util.Arrays;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.dd.plist.NSObject;
import com.dd.plist.NSArray;
import com.dd.plist.NSDate;
import com.dd.plist.NSString;
import com.dd.plist.NSNumber;
import com.dd.plist.NSData;
import com.dd.plist.PropertyListFormatException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.TskCoreException;
import org.xml.sax.SAXException;
import org.sleuthkit.autopsy.corecomponentinterfaces.FileTypeViewer;

/**
 * PListViewer - a file viewer for binary plist files.
 *
 */
public class PListViewer extends javax.swing.JPanel implements FileTypeViewer, ExplorerManager.Provider {

    private static final long serialVersionUID = 1L;
    private static final String[] MIMETYPES = new String[]{"application/x-bplist"};
    private static final Logger LOGGER = Logger.getLogger(PListViewer.class.getName());

    private final org.openide.explorer.view.OutlineView outlineView;
    private final Outline outline;
    private ExplorerManager explorerManager;

    private NSDictionary rootDict;

    /**
     * Creates new form PListViewer
     */
    public PListViewer() {

        // Create an Outlineview and add to the panel
        outlineView = new org.openide.explorer.view.OutlineView();

        initComponents();

        outline = outlineView.getOutline();

        ((DefaultOutlineModel) outline.getOutlineModel()).setNodesColumnLabel("Key");

        outlineView.setPropertyColumns(
                "Type", Bundle.PListNode_TypeCol(),
                "Value", Bundle.PListNode_ValueCol());

        customize();
    }

    @NbBundle.Messages({"PListNode.KeyCol=Key",
        "PListNode.TypeCol=Type",
        "PListNode.ValueCol=Value"})

    private void customize() {

        outline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        outline.setRootVisible(false);
        if (null == explorerManager) {
            explorerManager = new ExplorerManager();
        }

        //outline.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        plistTableScrollPane.setViewportView(outlineView);

        outline.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        this.setVisible(true);
        outline.setRowSelectionAllowed(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        plistTableScrollPane = new javax.swing.JScrollPane();
        hdrPanel = new javax.swing.JPanel();
        exportButton = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.BorderLayout());

        plistTableScrollPane.setBorder(null);
        plistTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        plistTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jPanel1.add(plistTableScrollPane, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(exportButton, org.openide.util.NbBundle.getMessage(PListViewer.class, "PListViewer.exportButton.text")); // NOI18N
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout hdrPanelLayout = new javax.swing.GroupLayout(hdrPanel);
        hdrPanel.setLayout(hdrPanelLayout);
        hdrPanelLayout.setHorizontalGroup(
            hdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hdrPanelLayout.createSequentialGroup()
                .addContainerGap(320, Short.MAX_VALUE)
                .addComponent(exportButton)
                .addContainerGap())
        );
        hdrPanelLayout.setVerticalGroup(
            hdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hdrPanelLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(exportButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(hdrPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(hdrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages({"PListViewer.ExportSuccess.message=Plist file exported successfully",
        "PListViewer.ExportFailed.message=Plist file export failed.",})

    /**
     * Handles the Export button pressed action
     */
    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(Case.getCurrentCase().getExportDirectory()));
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));

        final int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".xml")) { // NON-NLS
                selectedFile = new File(selectedFile.toString() + ".xml"); // NON-NLS
            }

            try {
                //Save the propery list as XML
                PropertyListParser.saveAsXML(this.rootDict, selectedFile);
                JOptionPane.showMessageDialog(this,
                        String.format("Plist file exported successfully to %s ", selectedFile.getName()),
                        Bundle.PListViewer_ExportSuccess_message(),
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        String.format("Failed to export plist file to %s ", selectedFile.getName()),
                        Bundle.PListViewer_ExportFailed_message(),
                        JOptionPane.ERROR_MESSAGE);

                LOGGER.log(Level.SEVERE, "Error exporting plist to XML file " + selectedFile.getName(), ex);
            }
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    /**
     * Returns mime types supported by this viewer
     *
     * @return list of supported mime types
     */
    @Override
    public List<String> getSupportedMIMETypes() {
        return Arrays.asList(MIMETYPES);
    }

    /**
     * Sets the file to be displayed in the viewer
     *
     * @param file file to display
     */
    @Override
    public void setFile(final AbstractFile file) {
        processPlist(file);
    }

    /**
     * Returns the viewer component
     *
     * @return the viewer component
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     * Resets the viewer component
     *
     */
    @Override
    public void resetComponent() {
        rootDict = null;
    }

    /**
     * Process the given Plist file
     *
     * @param plistFile -
     *
     * @return none
     */
    private void processPlist(final AbstractFile plistFile) {

        final byte[] plistFileBuf = new byte[(int) plistFile.getSize()];
        try {
            plistFile.read(plistFileBuf, 0, plistFile.getSize());
        } catch (TskCoreException ex) {
            LOGGER.log(Level.SEVERE, "Error reading bytes of plist file.", ex);
        }

        final List<PropKeyValue> plist;
        try {
            plist = parsePList(plistFileBuf);
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    setupTable(plist);
                    return null;
                }

                @Override
                protected void done() {
                    super.done();
                    setColumnWidths();
                }
            }.execute();

        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException ex) {
            LOGGER.log(Level.SEVERE, String.format("Error parsing plist for file (obj_id = %d)", plistFile.getId()), ex);
        } 
    }

    /**
     * Sets up the columns in the display table
     *
     * @param tableRows
     */
    private void setupTable(final List<PropKeyValue> tableRows) {
        explorerManager.setRootContext(new AbstractNode(Children.create(new PListRowFactory(tableRows), true)));
    }

    /**
     * Sets up the column widths
     *
     */
    private void setColumnWidths() {
        final int margin = 4;
        final int padding = 8;

        // find the maximum width needed to fit the values for the first N rows, at most
        final int rows = Math.min(20, outline.getRowCount());
        for (int col = 0; col < outline.getColumnCount(); col++) {
            final int columnWidthLimit = 2000;
            int columnWidth = 0;

            for (int row = 0; row < rows; row++) {
                final TableCellRenderer renderer = outline.getCellRenderer(row, col);
                final Component comp = outline.prepareRenderer(renderer, row, col);

                columnWidth = Math.max(comp.getPreferredSize().width, columnWidth);
            }

            columnWidth += 2 * margin + padding; // add margin and regular padding
            columnWidth = Math.min(columnWidth, columnWidthLimit);
            outline.getColumnModel().getColumn(col).setPreferredWidth(columnWidth);
        }
    }

    /**
     * Parses the given plist key/value
     */
    @NbBundle.Messages({"PListViewer.DataType.message=Binary Data value not shown"})
    private PropKeyValue parseProperty(final String key, final NSObject value) {
        if (value == null) {
            return null;
        } else if (value instanceof NSString) {
            return new PropKeyValue(key, PropertyType.STRING, value.toString());
        } else if (value instanceof NSNumber) {
            final NSNumber number = (NSNumber) value;
            if (number.isInteger()) {
                return new PropKeyValue(key, PropertyType.NUMBER, number.longValue());
            } else if (number.isBoolean()) {
                return new PropKeyValue(key, PropertyType.BOOLEAN, number.boolValue());
            } else {
                return new PropKeyValue(key, PropertyType.NUMBER, number.floatValue());
            }
        } else if (value instanceof NSDate) {
            final NSDate date = (NSDate) value;
            return new PropKeyValue(key, PropertyType.DATE, date.toString());
        } else if (value instanceof NSData) {
            return new PropKeyValue(key, PropertyType.DATA, Bundle.PListViewer_DataType_message());
        } else if (value instanceof NSArray) {
            final List<PropKeyValue> children = new ArrayList<>();
            final NSArray array = (NSArray) value;

            final PropKeyValue pkv = new PropKeyValue(key, PropertyType.ARRAY, array);
            for (int i = 0; i < array.count(); i++) {
                children.add(parseProperty("", array.objectAtIndex(i)));
            }

            pkv.setChildren(children.toArray(new PropKeyValue[children.size()]));
            return pkv;
        } else if (value instanceof NSDictionary) {
            final List<PropKeyValue> children = new ArrayList<>();
            final NSDictionary dict = (NSDictionary) value;

            final PropKeyValue pkv = new PropKeyValue(key, PropertyType.DICTIONARY, dict);
            for (final String key2 : ((NSDictionary) value).allKeys()) {
                final NSObject obj = ((NSDictionary) value).objectForKey(key2);
                children.add(parseProperty(key2, obj));
            }

            pkv.setChildren(children.toArray(new PropKeyValue[children.size()]));
            return pkv;
        } else {
            LOGGER.log(Level.SEVERE, "Can''t parse Plist for key = {0} value of type {1}", new Object[]{key, value.getClass()});
        }

        return null;
    }

    /**
     * Parses given binary stream and extracts Plist key/value
     *
     * @param plistbytes
     *
     * @return list of PropKeyValue
     */
    private List<PropKeyValue> parsePList(final byte[] plistbytes) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {

        final List<PropKeyValue> plist = new ArrayList<>();
        rootDict = (NSDictionary) PropertyListParser.parse(plistbytes);

        final String[] keys = rootDict.allKeys();
        for (final String key : keys) {
            final PropKeyValue pkv = parseProperty(key, rootDict.objectForKey(key));
            if (null != pkv) {
                plist.add(pkv);
            }
        }

        return plist;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    /**
     * Plist property type
     */
    enum PropertyType {
        STRING,
        NUMBER,
        BOOLEAN,
        DATE,
        DATA,
        ARRAY,
        DICTIONARY
    };

    /**
     * Encapsulates a Plist property
     *
     */
    final static class PropKeyValue {

        private final String key;
        private final PropertyType type;
        private final Object value;

        private PropKeyValue[] children;

        PropKeyValue(String key, PropertyType type, Object value) {
            this.key = key;
            this.type = type;
            this.value = value;

            this.children = null;
        }

        /**
         * Copy constructor
         */
        PropKeyValue(PropKeyValue other) {
            this.key = other.getKey();
            this.type = other.getType();
            this.value = other.getValue();

            this.setChildren(other.getChildren());
        }

        String getKey() {
            return this.key;
        }

        PropertyType getType() {
            return this.type;
        }

        Object getValue() {
            return this.value;
        }

        /**
         * Returns an array of children, if any.
         *
         * @return
         */
        PropKeyValue[] getChildren() {
            if (children == null) {
                return null;
            }

            // return a copy
            return Arrays.stream(children)
                    .map(child -> new PropKeyValue(child))
                    .toArray(PropKeyValue[]::new);
        }

        void setChildren(final PropKeyValue... children) {
          
            this.children = Arrays.stream(children)
                    .map(child -> new PropKeyValue(child))
                    .toArray(PropKeyValue[]::new);
            
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel hdrPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane plistTableScrollPane;
    // End of variables declaration//GEN-END:variables
}
